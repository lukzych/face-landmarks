package pl.luki.face_landmarks.service;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import pl.luki.face_landmarks.response.DetectionResult;
import pl.luki.face_landmarks.response.Face;
import pl.luki.face_landmarks.response.Point;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LandmarkService {

    private OrtEnvironment env;
    private OrtSession landmarker;

    @PostConstruct
    void loadModel() throws Exception {
        env = OrtEnvironment.getEnvironment();
        landmarker = env.createSession(getClass().getResourceAsStream("/models/landmarks68.onnx").readAllBytes());
    }

    
    public DetectionResult detect(BufferedImage img) throws Exception {
        int w = img.getWidth(), h = img.getHeight();

        
        BufferedImage small = new BufferedImage(112, 112, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = small.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, 112, 112, null);

        float[] data = new float[3 * 112 * 112];
        for (int y = 0; y < 112; y++)
            for (int x = 0; x < 112; x++) {
                int rgb = small.getRGB(x, y), i = y * 112 + x;
                data[i] = (rgb >> 16 & 0xFF) / 255f;
                data[112 * 112 + i] = (rgb >> 8 & 0xFF) / 255f;
                data[2 * 112 * 112 + i] = (rgb & 0xFF) / 255f;
            }

        
        float[] p;
        try (var t = OnnxTensor.createTensor(env, FloatBuffer.wrap(data), new long[]{1, 3, 112, 112});
             var r = landmarker.run(Map.of("input", t))) {
            p = ((float[][]) r.get(0).getValue())[0];
        }

        List<Point> points = new ArrayList<>();
        for (int i = 0; i < p.length; i += 2)
            points.add(new Point(p[i] * w, p[i + 1] * h));

        return new DetectionResult(w, h, List.of(new Face(0, 0, w, h, points)));
    }
}
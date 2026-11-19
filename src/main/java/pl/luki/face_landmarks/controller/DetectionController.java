package pl.luki.face_landmarks.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import pl.luki.face_landmarks.service.LandmarkService;


@RestController 
@RequestMapping("/api")
public class DetectionController {
    
    private final LandmarkService landmarkService;

     public DetectionController(LandmarkService landmarkService) {
        this.landmarkService = landmarkService;
    }


    @PostMapping(path = "/detect", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> detect(@RequestParam("file") MultipartFile file) {
        try {
            BufferedImage img = ImageIO.read(file.getInputStream());
            if (img == null) {
                return ResponseEntity.badRequest().body("Nieobsługiwany format zdjęcia");
            }
            return ResponseEntity.ok(landmarkService.detect(img));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Błąd wykrywania: " + e.getMessage());
        }
    }
}

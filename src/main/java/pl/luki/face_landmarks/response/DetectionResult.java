package pl.luki.face_landmarks.response;

import java.util.List;

public record DetectionResult(int width, int height, List<Face> faces) {}
package pl.luki.face_landmarks.response;

import java.util.List;

public record Face(int x, int y, int w, int h, List<Point> points) {}
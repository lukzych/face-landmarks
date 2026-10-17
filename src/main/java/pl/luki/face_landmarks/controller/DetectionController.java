package pl.luki.face_landmarks.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/api")
public class DetectionController {
    

    @GetMapping("/hello")
    public String hello(){
        return "ELOOO!";
    }
}

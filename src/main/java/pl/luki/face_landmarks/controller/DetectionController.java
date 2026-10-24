package pl.luki.face_landmarks.controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.nio.file.Path;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController 
@RequestMapping("/api")
public class DetectionController {
    

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file){
		
		try {
            Path dir = Paths.get("/workspace/uploads");
            Files.createDirectories(dir);                         
            String filename = UUID.randomUUID() + ".jpg";         
            file.transferTo(dir.resolve(filename));
            return ResponseEntity.ok("File uploaded successfully: " + filename);
        } catch (Exception e) {
            e.printStackTrace();                                  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Błąd zapisu: " + e.getMessage());     
        }
	}
}

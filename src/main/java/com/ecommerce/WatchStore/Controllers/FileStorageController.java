package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.Services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/file")
public class FileStorageController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        if (fileName != null) {
            return new ResponseEntity<>("File uploaded successfully. Stored as: " + fileName, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to upload file.", HttpStatus.BAD_REQUEST);
        }
    }
}

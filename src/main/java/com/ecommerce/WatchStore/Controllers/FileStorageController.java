package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.Services.FileStorageService;
import io.opencensus.resource.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/file")
public class FileStorageController {

    @Autowired
    private FileStorageService fileStorageService;
    @Value("${file.upload.directory}")
    private String uploadPath;

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

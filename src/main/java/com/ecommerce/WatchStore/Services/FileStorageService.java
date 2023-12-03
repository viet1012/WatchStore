package com.ecommerce.WatchStore.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;

@Service
public class FileStorageService {

    @Value("${file.upload.directory}")
    private String uploadPath;
    public String storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return null; // Hoặc xử lý tệp rỗng theo nhu cầu
            }

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            File targetFile = new File(uploadPath, fileName);

            // Lưu tệp vào hệ thống tệp của máy chủ
            file.transferTo(targetFile);

            // Tạo đường dẫn URL linh hoạt sử dụng ServletUriComponentsBuilder
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(fileName)
                    .toUriString();

            return fileDownloadUri;
        } catch (IOException ex) {
            throw new RuntimeException("Không thể lưu tệp " + file.getOriginalFilename(), ex);
        }
    }
}

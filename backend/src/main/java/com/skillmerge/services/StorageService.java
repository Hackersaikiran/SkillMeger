package com.skillmerge.services;

import com.skillmerge.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageService {

    @Value("${app.storage.upload-dir}")
    private String uploadDir;

    @Value("${app.storage.base-url}")
    private String baseUrl;

    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename() == null ? "resume" : file.getOriginalFilename());
        String extension = "";
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > -1) {
            extension = filename.substring(dotIndex);
        }
        String storedName = UUID.randomUUID() + extension;
        try {
            Path directory = Paths.get(uploadDir);
            Files.createDirectories(directory);
            Path target = directory.resolve(storedName);
            Files.copy(file.getInputStream(), target);
            return baseUrl + "/" + storedName;
        } catch (IOException e) {
            throw new BadRequestException("Failed to store resume");
        }
    }
}

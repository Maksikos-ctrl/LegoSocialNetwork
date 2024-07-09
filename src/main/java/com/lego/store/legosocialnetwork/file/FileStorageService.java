package com.lego.store.legosocialnetwork.file;


import com.lego.store.legosocialnetwork.lego.Lego;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {
    @Value("${application.file.uploads.photos-output-path}")
    private String fileUploadPath;
    public Object saveFile(
               @NotNull MultipartFile sourceFile,
               @NotNull Integer userId
    ) {
        final String fileUploadSubPath = "users" + separator + userId;
        return uploadFile(sourceFile, fileUploadSubPath);

    }

    private String uploadFile(
            @NotNull MultipartFile sourceFile,
            @NotNull String fileUploadSubPath
    ) {
        final String fileUploadPath = this.fileUploadPath + separator + fileUploadSubPath;
        File targetFolder = new File(fileUploadPath);
        if (!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdirs();
            if (!folderCreated) {
                log.warn("Failed to create the target folder: " + targetFolder);
                return null;
            }
        }

        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        // e.g ./uploads/users/1/123456789.jpg
        String targetFileName = fileUploadPath + separator + System.currentTimeMillis() + "." + fileExtension;
        Path targetPath = Paths.get(targetFileName);
        try {
            Files.write(targetPath, sourceFile.getBytes());
            log.info("File saved to: " + targetFileName);
            return targetFileName;
        } catch (Exception e) {
            log.error("File was not saved", e);
        }
        return null;
    }

    private String getFileExtension(String originalFilename) {
        if (originalFilename == null || originalFilename.isEmpty()) {
            return null;
        }
        final int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return null;
        }
        //! e.g .JPG -> .jpg
        return originalFilename.substring(lastDotIndex + 1).toLowerCase();
    }
}

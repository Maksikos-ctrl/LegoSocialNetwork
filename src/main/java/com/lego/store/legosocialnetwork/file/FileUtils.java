package com.lego.store.legosocialnetwork.file;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileUtils {
    public static byte[] readFileFromLocation(String fileUrl) {
        if (StringUtils.isBlank(fileUrl)) {
            return null;
        }

        try {
            Path filePath = Paths.get(fileUrl);
            return Files.readAllBytes(filePath);

        } catch (IOException e) {
            log.warn("Failed to read file from location: " + fileUrl);

        }
        return new byte[0];
    }
}

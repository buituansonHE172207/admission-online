package com.kas.demo_auth.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kas.demo_auth.model.GeneralInformation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileService {

    private static final String UPLOAD_DIR = "src/main/resources/static/pdf/";

    public void saveFile(MultipartFile file, GeneralInformation generalInformation) throws IOException {
        // Delete old file if it exists
        if (generalInformation.getPdfName() != null)
            deleteFile(generalInformation.getPdfName());

        // Check file type
        if (!isPdfFile(file)) {
            throw new IllegalArgumentException("Chỉ có thể tải file dạng pdf!");
        }

        // Save new file
        String originalFilename = file.getOriginalFilename();
        String newFilename = generateNewFilename(originalFilename);
        String filePath = UPLOAD_DIR + newFilename;

        var is = file.getInputStream();
        Files.copy(is, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        // Update file name in model
        generalInformation.setPdfName(newFilename);
    }

    public void deleteFile(String pdfPath) {
        String fileToDelete = UPLOAD_DIR + pdfPath;
        try {
            Files.deleteIfExists(Paths.get(fileToDelete));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateNewFilename(String originalFilename) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "file_" + timestamp + ".pdf";
    }

    private boolean isPdfFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.equals("application/pdf");
    }
}

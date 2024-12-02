package com.tripwhiz.tripwhizuserback.util.file.service;

import com.tripwhiz.tripwhizuserback.util.file.domain.AttachFile;
import com.tripwhiz.tripwhizuserback.util.file.exception.UploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class UploadService {

    @Value("${com.tripwhiz.upload.productpath}")
    private String uploadFolder;

    @Value("${com.tripwhiz.upload.qrcodepath}")
    private String qrImageFolder;

    public List<AttachFile> uploadFiles(MultipartFile[] files) {
        List<AttachFile> uploadedFiles = new ArrayList<>();
        if (files == null || files.length == 0) {
            return uploadedFiles;
        }

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            File destination = new File(uploadFolder, fileName);
            try {
                FileCopyUtils.copy(file.getBytes(), destination);
                uploadedFiles.add(new AttachFile(0, fileName));
                log.info("File uploaded successfully: {}", fileName);
            } catch (IOException e) {
                throw new UploadException("Failed to upload file: " + e.getMessage());
            }
        }
        return uploadedFiles;
    }

    public AttachFile uploadQRCodeFile(String fileName) {
        File qrFile = new File(qrImageFolder, fileName);
        if (!qrFile.exists()) {
            throw new UploadException("QR Code file does not exist: " + fileName);
        }
        log.info("QR Code file registered successfully: {}", fileName);
        return new AttachFile(0, fileName);
    }
}

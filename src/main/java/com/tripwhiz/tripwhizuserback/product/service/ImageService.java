package com.tripwhiz.tripwhizuserback.product.service;

import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Arrays;

@Service
public class ImageService {


    @Value("${UPLOAD_DIR:/app/upload}")
    private String uploadDir;

    @Autowired
    private ProductRepository productRepository;

//    @Transactional
//    public void saveImagesWithUrl(String directoryPath, Long pno) {
//        logger.info("Starting to save images for product ID: {} from directory: {}", pno, directoryPath);
//
//        Product product = productRepository.findById(pno)
//                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + pno));
//
//        File directory = new File(directoryPath);
//        if (directory.exists() && directory.isDirectory()) {
//            logger.info("Directory found: {}", directoryPath);
//
//            File[] files = directory.listFiles();
//            if (files != null && files.length > 0) {
//                Arrays.stream(files)
//                        .filter(File::isFile)
//                        .forEach(file -> {
//                            String fileName = file.getName();
//                            String fileUrl = fileName;
//
//                            // Product에 Image 추가
//                            product.addImage(fileName);
//                            logger.info("Added image to product - Filename: {}, URL: {}", fileName, fileUrl);
//                        });
//
//                // Product와 이미지를 함께 저장
//                productRepository.save(product);
//                logger.info("Product and images saved successfully.");
//            } else {
//                logger.warn("No files found in directory: {}", directoryPath);
//            }
//        } else {
//            logger.error("Directory does not exist or is not a directory: {}", directoryPath);
//        }
//    }

    @Transactional
    public void saveImagesFromDirectory(Long pno) {
        logger.info("Saving images for product ID: {}", pno);

        Product product = productRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + pno));

        File directory = new File(uploadDir);
        if (directory.exists() && directory.isDirectory()) {

            File[] files = directory.listFiles();
            if (files != null) {
                Arrays.stream(files)
                        .filter(File::isFile)
                        .forEach(file -> {
                            String fileName = file.getName();
                            product.addImage(fileName);
                            logger.info("Added image to product - Filename: {}", fileName);
                        });

                productRepository.save(product);
                logger.info("Product and images saved successfully.");
            } else {
                logger.warn("No files found in directory: {}", uploadDir);
            }
        } else {
            logger.error("Directory does not exist: {}", uploadDir);
        }
    }

    @Transactional
    public String uploadImage(MultipartFile file, Long pno) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Product product = productRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + pno));
        product.addImage(fileName);
        productRepository.save(product);

        return "/images/" + fileName;
    }
}

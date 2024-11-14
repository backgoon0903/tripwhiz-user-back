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


    @Autowired
    private ProductRepository productRepository;

    // 이미지가 호스팅된 서버의 베이스 URL
    private static final String BASE_URL = "http://10.10.10.199/images/";

    @Transactional
    public void saveImagesWithUrl(String directoryPath, Long pno) {

        Product product = productRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + pno));

        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {

            File[] files = directory.listFiles();
            if (files != null && files.length > 0) {
                Arrays.stream(files)
                        .filter(File::isFile)
                        .forEach(file -> {
                            String fileName = file.getName();
                            String fileUrl = BASE_URL + fileName;  // BASE_URL을 사용하여 전체 URL을 생성

                            // Product에 Image 추가
                            product.addImage(fileName, fileUrl);
                        });

                // Product와 이미지를 함께 저장
                productRepository.save(product);
            }
        }
    }
}

package com.tripwhiz.tripwhizuserback.product.service;

import com.tripwhiz.tripwhizuserback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizuserback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO;
import com.tripwhiz.tripwhizuserback.product.repository.ProductRepository;
import com.tripwhiz.tripwhizuserback.util.CustomFileUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final CustomFileUtil customFileUtil;

    public PageResponseDTO<ProductListDTO> list(PageRequestDTO pageRequestDTO) {

        log.info("2------------------------------");
        log.info(productRepository.listByCno(pageRequestDTO));

        return productRepository.listByCno(pageRequestDTO);
    }

    public Optional<ProductReadDTO> getProductById(Long pno) {

        return productRepository.read(pno);

    }

//    // 상품 정보와 이미지를 함께 조회하는 메서드
//    public Optional<ProductReadDTO> getProductWithImage(Long pno) {
//        // 상품 정보 조회
//        Optional<ProductReadDTO> productOptional = productRepository.read(pno);
//
//        // 상품이 존재할 경우 파일 조회 수행
//        return productOptional.map(product -> {
//            // 파일명으로 이미지 조회
//            if (product.getFileName() != null) {
//                ResponseEntity<Resource> image = customFileUtil.getFile(product.getFileName());
//                // 여기에서 필요한 경우 image 정보를 추가하거나 처리할 수 있음
//                log.info("Image retrieved for product: {}", product.getFileName());
//            }
//            return product;
//        });
//    }



}

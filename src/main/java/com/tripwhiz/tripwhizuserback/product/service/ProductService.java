package com.tripwhiz.tripwhizuserback.product.service;

import com.tripwhiz.tripwhizuserback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizuserback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO;
import com.tripwhiz.tripwhizuserback.product.repository.ProductRepository;
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

    public PageResponseDTO<ProductListDTO> list(PageRequestDTO pageRequestDTO) {

        return productRepository.listByCno(1L, pageRequestDTO);

    }

    public Optional<ProductReadDTO> getProductById(Long pno) {

        return productRepository.read(pno);

    }

}

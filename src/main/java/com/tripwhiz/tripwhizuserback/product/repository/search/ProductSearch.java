package com.tripwhiz.tripwhizuserback.product.repository.search;

import com.tripwhiz.tripwhizuserback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizuserback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.dto.ProductListDTO;
import com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductSearch {

    Page<Product> list(Pageable pageable);

    PageResponseDTO<ProductListDTO> listByDno(Long dno, PageRequestDTO pageRequestDTO);

    Optional<ProductReadDTO> read(Long pno);

}

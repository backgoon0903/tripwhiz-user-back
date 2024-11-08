package com.tripwhiz.tripwhizuserback.product.repository.search;

import com.tripwhiz.tripwhizuserback.product.dto.ProductReadDTO;

import java.util.Optional;

public interface ProductSearch {

    Optional<ProductReadDTO> read(Long pno);

}

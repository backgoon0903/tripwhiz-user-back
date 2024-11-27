package com.tripwhiz.tripwhizuserback.cart.repository;

import com.tripwhiz.tripwhizuserback.cart.domain.Cart;
import com.tripwhiz.tripwhizuserback.cart.dto.CartListDTO;
import com.tripwhiz.tripwhizuserback.cart.dto.CartProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT new com.tripwhiz.tripwhizuserback.cart.dto.CartListDTO(c.member.email, c.bno, c.product.pno, c.qty) " +
            "FROM Cart c")
    List<CartListDTO> findAllCartItems();

    // 특정 제품이 장바구니에 있는지 확인 (회원 고려 X)
    @Query("SELECT c FROM Cart c WHERE c.product.pno = :pno")
    Optional<Cart> findByProduct(Long pno);

    // 장바구니에 있는 모든 항목 조회
//    @Query("SELECT new com.tripwhiz.tripwhizuserback.cart.dto.CartProductDTO(" +
//            "c.product.pno, c.product.pname, c.product.price, c.qty) " +
//            "FROM Cart c")
//    List<CartProductDTO> findAllCartItems();

}

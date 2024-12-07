package com.tripwhiz.tripwhizuserback.cart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripwhiz.tripwhizuserback.cart.domain.Cart;
import com.tripwhiz.tripwhizuserback.cart.dto.CartListDTO;
import com.tripwhiz.tripwhizuserback.cart.repository.CartRepository;
import com.tripwhiz.tripwhizuserback.member.domain.MemberEntity;
import com.tripwhiz.tripwhizuserback.product.domain.Product;
import com.tripwhiz.tripwhizuserback.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;


    // application.yml 파일에서 User API URL을 불러와 변수에 저장
    @Value("${server.store.owner.base.url}")
    private String adminApiUrl;

    // 장바구니에 물건 추가
    public void addToCart(CartListDTO cartListDTO) {

        Product product = Product.builder().pno(cartListDTO.getPno()).build();
        MemberEntity member = MemberEntity.builder().email(cartListDTO.getEmail()).build();

        // 장바구니에서 해당 제품 찾기
        Optional<Cart> existingCart = cartRepository.findByProduct(cartListDTO.getPno());

        if (existingCart.isPresent()) {
            // 기존 제품이 있으면 수량 업데이트
            Cart cart = existingCart.get();
//            cart.setQty();
            cart.setQty(cart.getQty() + cartListDTO.getQty()); // 수량 업데이트
            cartRepository.save(cart); // 변경사항 저장
        } else {
            // 없으면 새로 추가
            Cart cart = Cart.builder()
                    .product(product)
                    .qty(cartListDTO.getQty())
                    .member(member)
                    .build();
            cartRepository.save(cart);
        }

    }

    // 장바구니 목록 조회
    public List<CartListDTO> list(String email) {

        List<CartListDTO> cartItems = cartRepository.findCartItemsByMemberEmail(email);

        return cartItems.stream()
                .map(cart -> CartListDTO.builder()
                        .email(cart.getEmail())
                        .bno(cart.getBno())
                        .pno(cart.getPno())
                        .pname(cart.getPname())
                        .price(cart.getPrice())
                        .qty(cart.getQty())
                        .build())
                .collect(Collectors.toList());

    }

    // 상품 개별 삭제
    public void softDeleteByProduct(String email, Long pno) {
        Cart cart = cartRepository.findByMemberEmailAndProductPno(email, pno)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        cart.softDelete(); // 엔티티의 softDelete 메서드 호출
    }

    // 상품 수량 변경
    public void changeQty(Long pno, int qty) {
        // 장바구니 항목 조회
        Cart cart = cartRepository.findById(pno)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        // 수량 변경
        if (qty < 0) {
            throw new IllegalArgumentException("Quantity cannot be less than zero.");
        }

        cart.setQty(qty); // 새로운 수량 설정
        cartRepository.save(cart); // 변경 저장
    }

    // 장바구니 전체 비우기
    public void softDeleteAll(String email) {
        cartRepository.softDeleteAllByEmail(email);
    }


    // Admin API에 상품 정보를 전송하는 메서드
//    private void sendCartToAdminApi(CartListDTO cartListDTO, String endpoint, HttpMethod httpMethod) {
//        try {
//            // MultiValueMap으로 요청 데이터 구성
//            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//
//            // JSON 데이터를 문자열로 변환하여 추가
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonProduct = objectMapper.writeValueAsString(cartListDTO);
//            body.add("cartListDTO", jsonProduct);
//
////             파일 데이터를 추가
//            if (imageFiles != null) {
//                for (MultipartFile file : imageFiles) {
//                    body.add("imageFiles", new ByteArrayResource(file.getBytes()) {
//                        @Override
//                        public String getFilename() {
//                            return file.getOriginalFilename();
//                        }
//                    });
//                }
//            }
//
//            // HTTP 헤더 설정
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//            // HttpEntity 생성
//            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
//
//            // Admin API Endpoint 설정
//            String adminApiEndpoint = adminApiUrl + endpoint;
//
//            // Admin API로 POST 요청을 보내고 응답 받기
//            ResponseEntity<Long> response = restTemplate.exchange(adminApiEndpoint, httpMethod, request, Long.class);
//
//            // 요청 성공 여부 확인
//            if (response.getStatusCode().is2xxSuccessful()) {
//                log.info("Cart successfully sent to Admin API, ID: {}", response.getBody());
//            } else {
//                log.error("Failed to send cart to Admin API: {}", response.getStatusCode());
//            }
//        } catch (Exception e) {
//            // 예외 발생 시 오류 로그 출력
//            log.error("Error sending cart to Admin API", e);
//        }
//    }

//    // Admin API로 장바구니 목록 전송
//    public void sendCart(String email) {
//
//        List<CartListDTO> cartList = list(email);
//
//        sendCartToAdminApi(cartList, "/api/cart/save");
//
//    }
//
//    private void sendCartToAdminApi(List<CartListDTO> cartList, String endpoint) {
//        try {
//            // JSON 데이터를 문자열로 변환
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonCartList = objectMapper.writeValueAsString(cartList);
//
//            log.info(jsonCartList);
//
//            // HTTP 요청 본문 및 헤더 설정
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            // Admin API Endpoint
//            String adminApiEndpoint = adminApiUrl + endpoint;
//
//            // API 요청 및 응답 처리
//            String response = restTemplate.postForObject(adminApiEndpoint, new HttpEntity<>(jsonCartList, headers), String.class);
//
//            log.info("---------------");
//            log.info("---------------");
//            log.info(response);
//            log.info("---------------3");
//            log.info("---------------4");
//
//            log.info("Cart list successfully sent to Admin API");
//        } catch (Exception e) {
//            log.error("Error sending cart list to Admin API", e);
//        }
//    }


}

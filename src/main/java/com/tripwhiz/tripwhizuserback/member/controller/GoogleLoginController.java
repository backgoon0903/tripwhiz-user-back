package com.tripwhiz.tripwhizuserback.member.controller;

import com.tripwhiz.tripwhizuserback.member.dto.MemberDTO;
import com.tripwhiz.tripwhizuserback.member.dto.TokenResponseDTO;
import com.tripwhiz.tripwhizuserback.member.service.GoogleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@Log4j2
@RequiredArgsConstructor
public class GoogleLoginController {

    private final GoogleService googleService;

    // Google 로그인 시 토큰 생성 엔드포인트
    @RequestMapping("google")
    public ResponseEntity<TokenResponseDTO> googleToken(String accessToken) {

        log.info("Google access token:" +accessToken);

        // 구글 인증 토큰을 사용하여 사용자 정보를 조회
        MemberDTO memberDTO = googleService.authGoogle(accessToken);

        log.info(memberDTO);

        log.info("=======================================1");

        return null;
    }
}

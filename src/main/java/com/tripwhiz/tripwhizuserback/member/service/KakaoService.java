//package com.tripwhiz.tripwhizuserback.member.service;
//
//
//import com.tripwhiz.tripwhizuserback.member.domain.MemberEntity;
//import com.tripwhiz.tripwhizuserback.member.dto.MemberDTO;
//import com.tripwhiz.tripwhizuserback.member.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponents;
//import org.springframework.web.util.UriComponentsBuilder;
//
//
//import java.util.*;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//@Log4j2
//public class KakaoService {
//
//    private final MemberRepository memberRepository;
//
//    // 카카오 인증을 통해 사용자 정보를 가져오는 메서드
//    public MemberDTO authKakao(String accessToken) {
//
//        log.info("---------authKakao-------");
//
//        // 카카오 액세스 토큰을 사용하여 이메일을 가져옴
//        String email = getKakaoAccountInfo(accessToken, "email");
//        String nickname = getKakaoAccountInfo(accessToken, "nickname");
//        log.info("email: " + email);
//        log.info("nickname: " + nickname);
//
//        // 가져온 이메일로 사용자 정보를 조회
//        Optional<MemberEntity> result = memberRepository.findById(email);
//        MemberEntity memberEntity = null;
//        MemberDTO memberDTO = new MemberDTO();
//
//        // 기존 회원이면 해당 정보를 MemberDTO로 반환
//        if(result.isPresent()) {
//            memberEntity = result.get();
//            memberDTO.setEmail(memberEntity.getEmail());
//            memberDTO.setPw(memberEntity.getPw());
//            memberDTO.setName(memberEntity.getName());
//            memberDTO.setProvider(memberEntity.getProvider());
//
//            return memberDTO;
//        }
//
//        // 회원이 존재하지 않으면, 새로운 회원을 생성
//        String pw = UUID.randomUUID().toString(); // 새로운 사용자를 위한 임의의 비밀번호를 생성
//        MemberEntity newMember = MemberEntity.builder()
//                .email(email)
//                .pw(pw)
//                .name(nickname)
//                .provider("kakao")
//                .build();
//        memberRepository.save(newMember); // 새로운 회원을 데이터베이스에 저장
//
//        // 생성된 회원 정보를 MemberDTO로 반환
//        memberDTO.setEmail(email);
//        memberDTO.setPw(pw);
//        memberDTO.setName(nickname);
//        memberDTO.setProvider("kakao");
//
//        return memberDTO;
//    }
//
//    // 카카오 액세스 토큰을 사용하여 이메일을 가져오는 비공개 메서드
//    private String getKakaoAccountInfo(String accessToken, String field){
//
//
//        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";
//
//        // 토큰이 null이면 예외를 던짐
//        if(accessToken == null){
//            throw new RuntimeException("Access Token is null");
//        }
//        // 외부 API 요청을 위한 RestTemplate 객체
//        RestTemplate restTemplate = new RestTemplate();
//
//        // HTTP 헤더를 설정, Authorization과 Content-Type을 추가
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-Type","application/x-www-form-urlencoded");
//
//        // 헤더를 포함한 HTTP 엔터티를 생성
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        // 카카오 API에 요청을 보내고, 응답을 LinkedHashMap 형식으로 받음
//        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();
//        ResponseEntity<LinkedHashMap> response
//                = restTemplate.exchange(uriBuilder.toString(), HttpMethod.GET, entity, LinkedHashMap.class);
//
//        log.info(response);
//
//
//        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();
//        log.info("------------------------------------");
//        log.info(bodyMap);
//
//        // 카카오 계정 정보가 포함된 "kakao_account" 항목을 가져옴
//        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("kakao_account");
//        log.info("kakaoAccount: " + kakaoAccount);
//
//        // 이메일 또는 닉네임을 추출
//        if ("email".equals(field)) {
//            return kakaoAccount.get("email");
//        } else if ("nickname".equals(field)) {
//            LinkedHashMap<String, String> properties = bodyMap.get("properties");
//            return properties.get("nickname");
//        } else {
//            throw new RuntimeException("Invalid field: " + field);
//        }
//    }
//
//
//}

package com.tripwhiz.tripwhizuserback.member.service;

import com.tripwhiz.tripwhizuserback.member.domain.MemberEntity;
import com.tripwhiz.tripwhizuserback.member.dto.MemberDTO;
import com.tripwhiz.tripwhizuserback.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class KakaoService {

    private final MemberRepository memberRepository;

    public MemberDTO authKakao(String accessToken) {
        log.info("---------authKakao START-------");

        if (accessToken == null || accessToken.isEmpty()) {
            throw new RuntimeException("Access Token is null or empty");
        }

        // 사용자 정보 가져오기
        String email;
        String nickname;
        try {
            email = getKakaoAccountInfo(accessToken, "email");
            nickname = getKakaoAccountInfo(accessToken, "nickname");
        } catch (Exception e) {
            log.error("Failed to fetch Kakao user info: ", e);
            throw new RuntimeException("Failed to fetch Kakao user info");
        }

        log.info("Kakao User Info - Email: {}, Nickname: {}", email, nickname);

        // 사용자 조회 및 회원 가입 로직
        Optional<MemberEntity> result = memberRepository.findById(email);
        MemberDTO memberDTO = new MemberDTO();

        if (result.isPresent()) {
            MemberEntity memberEntity = result.get();
            memberDTO.setEmail(memberEntity.getEmail());
            memberDTO.setPw(memberEntity.getPw());
            memberDTO.setName(memberEntity.getName());
            memberDTO.setProvider(memberEntity.getProvider());
        } else {
            String pw = UUID.randomUUID().toString();
            MemberEntity newMember = MemberEntity.builder()
                    .email(email)
                    .pw(pw)
                    .name(nickname)
                    .provider("kakao")
                    .build();
            memberRepository.save(newMember);

            memberDTO.setEmail(email);
            memberDTO.setPw(pw);
            memberDTO.setName(nickname);
            memberDTO.setProvider("kakao");
        }

        log.info("---------authKakao END-------");
        return memberDTO;
    }

    private String getKakaoAccountInfo(String accessToken, String field) {
        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<LinkedHashMap> response = restTemplate.exchange(
                    kakaoGetUserURL, HttpMethod.GET, entity, LinkedHashMap.class);

            LinkedHashMap<String, Object> bodyMap = response.getBody();
            LinkedHashMap<String, Object> kakaoAccount = (LinkedHashMap<String, Object>) bodyMap.get("kakao_account");

            if ("email".equals(field)) {
                return (String) kakaoAccount.get("email");
            } else if ("nickname".equals(field)) {
                LinkedHashMap<String, String> properties = (LinkedHashMap<String, String>) bodyMap.get("properties");
                return properties.get("nickname");
            } else {
                throw new IllegalArgumentException("Invalid field: " + field);
            }
        } catch (Exception e) {
            log.error("Error fetching Kakao account info: ", e);
            throw new RuntimeException("Failed to get Kakao account information");
        }
    }
}


package com.tripwhiz.tripwhizuserback.member.service;

import com.tripwhiz.tripwhizuserback.member.domain.MemberEntity;
import com.tripwhiz.tripwhizuserback.member.dto.MemberDTO;
import com.tripwhiz.tripwhizuserback.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class GoogleService {

    private final MemberRepository memberRepository;

    //구글 인증을 통해 사용자 정보를 가져오는 메서드
    public MemberDTO authGoogle(String accessToken) {

        log.info("---------authGoogle--------");

        // 구글 액세스 토큰을 사용하여 이메일을 가져옴
        String email = getEmailGoogleAccessToken(accessToken);
        log.info("email: " + email);

        // 가져온 이메일로 사용자 정보를 조회
        Optional<MemberEntity> result = memberRepository.findById(email);
        MemberEntity memberEntity = null;
        MemberDTO memberDTO = new MemberDTO();

        // 기존 회원이면 해당 정보를 MemberDTO로 반환
        if (result.isPresent()) {
            memberEntity = result.get();
            memberDTO.setEmail(memberEntity.getEmail());
            memberDTO.setPw(memberEntity.getPw());
            return memberDTO;
        }
        // 회원이 존재하지 않으면, 새로운 회원을 생성
        String pw = UUID.randomUUID().toString(); // 새로운 사용자를 위한 임의의 비밀번호를 생성
        MemberEntity newMember = MemberEntity.builder()
                .email(email)
                .pw(pw)
                .build();
        memberRepository.save(newMember); // 새로운 회원을 데이터베이스에 저장

        // 생성된 회원 정보를 MemberDTO로 반환
        memberDTO.setEmail(email);
        memberDTO.setPw(pw);
        memberDTO.setProvider("google");

        return memberDTO;
    }

    // 구글 액세스 토큰을 사용하여 이메일을 가져오는 비공개 메서드
    private String getEmailGoogleAccessToken(String accessToken) {

        String googleUserInfoURL = "https://www.googleapis.com/oauth2/v3/userinfo";

        // 토큰이 null이면 예외를 던짐
        if (accessToken == null) {
            throw new RuntimeException("Access Token is null");
        }

        // 외부 API 요청을 위한 RestTemplate 객체
        RestTemplate restTemplate = new RestTemplate();

        // HTTP 헤더를 설정, Authorization과 Content-Type을 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");

        // 헤더를 포함한 HTTP 엔터티를 생성
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Google API에 요청을 보내고, 응답을 Map 형식으로 받음
        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(googleUserInfoURL).build();
        ResponseEntity<Map> response = restTemplate.exchange(uriBuilder.toString(), HttpMethod.GET, entity, Map.class);

        // 응답 본문에서 유효한 사용자 정보 추출
        Map<String, Object> payload = response.getBody();

        log.info("Google API Response: {}", payload);

        // 사용자 이메일을 반환
        return (String) payload.get("email");

    }
}

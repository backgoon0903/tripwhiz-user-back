package com.tripwhiz.tripwhizuserback.luggage.service;

import com.tripwhiz.tripwhizuserback.luggage.dto.LuggageDTO;
import com.tripwhiz.tripwhizuserback.luggage.entity.Luggage;
import com.tripwhiz.tripwhizuserback.luggage.repository.LuggageRepository;
import com.tripwhiz.tripwhizuserback.member.domain.MemberEntity;
import com.tripwhiz.tripwhizuserback.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LuggageService {

    @Autowired
    private LuggageRepository luggageRepository;

    @Autowired
    private MemberRepository memberRepository;

    public void saveLuggage(LuggageDTO luggageDTO) {
        // 이메일을 기준으로 MemberEntity 검색
        MemberEntity member = memberRepository.findByEmail(luggageDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Member not found with email: " + luggageDTO.getEmail()));

        // Luggage 엔티티 생성 및 저장
        Luggage luggage = Luggage.builder()
                .startLat(luggageDTO.getStartPoint().getLat())
                .startLng(luggageDTO.getStartPoint().getLng())
                .endLat(luggageDTO.getEndPoint().getLat())
                .endLng(luggageDTO.getEndPoint().getLng())
                .member(member) // email로 연결된 MemberEntity
                .build();

        luggageRepository.save(luggage);
    }

    // 이메일을 기준으로 Luggage 목록 조회
    public List<Luggage> getLuggageByEmail(String email) {
        return luggageRepository.findByMemberEmail(email); // 이메일로 Luggage 목록 조회
    }
}

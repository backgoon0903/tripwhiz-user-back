package com.tripwhiz.tripwhizuserback.luggage.repository;

import com.tripwhiz.tripwhizuserback.luggage.entity.Luggage;
import com.tripwhiz.tripwhizuserback.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LuggageRepository extends JpaRepository<Luggage, Long> {

    // 이메일을 기준으로 Luggage 목록 조회
    List<Luggage> findByMemberEmail(String email); // member의 email을 기준으로 Luggage를 조회

}

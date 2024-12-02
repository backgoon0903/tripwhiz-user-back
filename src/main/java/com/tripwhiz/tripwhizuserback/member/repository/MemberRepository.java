package com.tripwhiz.tripwhizuserback.member.repository;

import com.tripwhiz.tripwhizuserback.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {
    // 이메일로 MemberEntity 조회
    Optional<MemberEntity> findByEmail(String email);
}

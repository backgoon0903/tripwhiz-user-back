package com.tripwhiz.tripwhizuserback.member.repository;

import com.tripwhiz.tripwhizuserback.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {
}

package com.koul.StudyTogether.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koul.StudyTogether.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}

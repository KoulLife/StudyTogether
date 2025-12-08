package com.koul.StudyTogether.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koul.StudyTogether.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}

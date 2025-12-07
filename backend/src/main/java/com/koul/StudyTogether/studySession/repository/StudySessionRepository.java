package com.koul.StudyTogether.studySession.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koul.StudyTogether.studySession.domain.StudySession;

import java.util.Optional;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {
    Optional<StudySession> findFirstByMemberIdAndStudyRoomIdAndEndedAtIsNull(Long memberId, Long roomId);
}

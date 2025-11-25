package com.koul.StudyTogether.studySession.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koul.StudyTogether.studySession.domain.StudySession;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {
}

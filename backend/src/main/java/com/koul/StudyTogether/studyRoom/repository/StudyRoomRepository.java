package com.koul.StudyTogether.studyRoom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koul.StudyTogether.studyRoom.domain.StudyRoom;

public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {
}

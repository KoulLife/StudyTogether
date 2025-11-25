package com.koul.StudyTogether.studyRoom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koul.StudyTogether.studyRoom.domain.MemberStudyRoom;

public interface MemberStudyRoomRepository extends JpaRepository<MemberStudyRoom, Long> {
}

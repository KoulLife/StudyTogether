package com.koul.StudyTogether.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koul.StudyTogether.chat.domain.ChatChannel;

import java.util.Optional;

public interface ChatChannelRepository extends JpaRepository<ChatChannel, Long> {

    /**
     * StudySession ID로 ChatChannel 조회
     */
    Optional<ChatChannel> findByStudySession_Id(Long studySessionId);
}
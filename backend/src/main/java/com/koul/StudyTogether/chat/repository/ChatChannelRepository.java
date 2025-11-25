package com.koul.StudyTogether.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koul.StudyTogether.chat.domain.ChatChannel;

public interface ChatChannelRepository extends JpaRepository<ChatChannel, Long> {
}

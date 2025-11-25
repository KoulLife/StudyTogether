package com.koul.StudyTogether.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koul.StudyTogether.chat.domain.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}

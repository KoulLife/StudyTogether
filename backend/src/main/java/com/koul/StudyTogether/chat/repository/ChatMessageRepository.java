package com.koul.StudyTogether.chat.repository;

import com.koul.StudyTogether.chat.domain.ChatChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.koul.StudyTogether.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * 채팅 채널별 메시지 조회
     */
    Page<ChatMessage> findByChatChannelIdOrderByCreatedAtDesc(Long chatChannelId, Pageable pageable);

    /**
     * 특정 시간 이후 메시지 조회
     */
    Page<ChatMessage> findByChatChannelAndCreatedAtAfterOrderByCreatedAtAsc(
            ChatChannel chatChannel,
            LocalDateTime createdAt,
            Pageable pageable);

    /**
     * 채팅 채널별 메시지 개수
     */
    long countByChatChannelId(Long chatChannelId);

    /**
     * 최근 메시지 조회 (명시적 쿼리)
     */
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatChannel.id = :chatChannelId " +
            "ORDER BY cm.createdAt DESC")
    Page<ChatMessage> findRecentMessages(@Param("chatChannelId") Long chatChannelId, Pageable pageable);
}

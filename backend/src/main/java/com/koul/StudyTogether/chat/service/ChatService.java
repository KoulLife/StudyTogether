package com.koul.StudyTogether.chat.service;

import com.koul.StudyTogether.chat.domain.ChatMessage;
import com.koul.StudyTogether.chat.dto.ChatMessageDto;
import com.koul.StudyTogether.chat.repository.ChatChannelRepository;
import com.koul.StudyTogether.chat.repository.ChatMessageRepository;
import com.koul.StudyTogether.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatChannelRepository chatChannelRepository;
    private final MemberRepository memberRepository;

    /**
     * 채팅 메시지 저장
     */
    @Transactional
    public ChatMessage saveMessage(ChatMessageDto messageDto) {

        log.info("Saving chat message: roomId={}, sender={}",
                messageDto.getRoomId(), messageDto.getSender());

        // ChatChannel, Member 조회
        var chatChannel = chatChannelRepository.findById(Long.parseLong(messageDto.getRoomId()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        var member = memberRepository.findById(messageDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 채팅 메시지 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .chatChannel(chatChannel)
                .member(member)
                .content(messageDto.getMessage())
                .build();

        return chatMessageRepository.save(chatMessage);
    }

    /**
     * 특정 채팅방의 메시지 목록 조회 (페이징)
     */
    public Page<ChatMessage> getMessagesByRoomId(Long roomId, Pageable pageable) {
        return chatMessageRepository.findByChatChannelIdOrderByCreatedAtDesc(roomId, pageable);
    }

    /**
     * 최근 메시지 조회
     */
    public Page<ChatMessage> getRecentMessages(Long roomId, int size) {
        return chatMessageRepository.findRecentMessages(roomId, PageRequest.of(0, size));
    }

    /**
     * 채팅방의 전체 메시지 개수 조회
     */
    public long countMessagesByRoomId(Long roomId) {
        return chatMessageRepository.countByChatChannelId(roomId);
    }
}

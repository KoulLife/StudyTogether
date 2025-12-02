package com.koul.StudyTogether.chat.controller;

import com.koul.StudyTogether.chat.dto.ChatMessageDto;
import com.koul.StudyTogether.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations  messagingTemplate;
    private final ChatService chatService;

    /**
     * websocket /pub/chat/message 로 들어오는 메시징 처리.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message) {
        log.info("Received message: type={}, sender={}, roomId={}",
                message.getType(), message.getSender(), message.getRoomId());

        switch (message.getType()) {
            case ENTER -> message.setMessage(message.getSender() + "님이 입장했습니다.");
            case LEAVE -> message.setMessage(message.getSender() + "님이 퇴장했습니다.");
            case TALK -> {
                try {
                    chatService.saveMessage(message);
                } catch (Exception e) {
                    log.error("Failed to save chat message: {}", e.getMessage(), e);  // 스택 트레이스 포함
                }
            }
        }

        // 메시지 발송
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}

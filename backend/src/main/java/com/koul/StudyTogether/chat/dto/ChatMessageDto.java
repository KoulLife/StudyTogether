package com.koul.StudyTogether.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageDto {

    /**
     * 메시지 타입: 입장, 채팅
     */
    public enum MessageType {
        ENTER, TALK, LEAVE
    }

    private MessageType type;   // 메시지 타입
    private String message; // 메시지
    private String roomId;  // 방 번호
    private String sender;  // 채팅을 보낸 사람
    private Long memberId;  // Member 엔티티와 연동
    private LocalDateTime createdAt;  // 메시지 전송 시간
}

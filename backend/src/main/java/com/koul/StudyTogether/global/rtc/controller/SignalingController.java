package com.koul.StudyTogether.global.rtc.controller;

import com.koul.StudyTogether.global.rtc.dto.SignalingMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

/**
 * WebRTC 시그널링 컨트롤러
 *
 * P2P(Peer-to-Peer) 연결을 위한 SDP(Session Description Protocol) 및
 * ICE Candidate 정보를 교환(Signaling)하는 역할
 * WebSocket(STOMP)을 통해 메시지를 라우팅
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class SignalingController {

    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * WebRTC Offer 메시지 처리
     * 나(Caller)의 미디어 정보를 상대방에게 전달
     *
     * @param message Offer 정보 (SDP 포함)
     */
    @MessageMapping("/peer/offer")
    public void offer(@Payload SignalingMessage message) {
        log.info("Signaling Offer: {}", message);
        messagingTemplate.convertAndSend("/sub/peer/room/" + message.getRoomId(), message);
    }

    /**
     * WebRTC Answer 메시지 처리
     * 상대방(Callee)이 제안(Offer)에 대한 응답을 보냄
     *
     * @param message Answer 정보 (SDP 포함)
     */
    @MessageMapping("/peer/answer")
    public void answer(@Payload SignalingMessage message) {
        log.info("Signaling Answer: {}", message);
        messagingTemplate.convertAndSend("/sub/peer/room/" + message.getRoomId(), message);
    }

    /**
     * ICE Candidate 메시지 처리
     * P2P 연결 가능한 네트워크 경로 후보군(Candidate) 정보를 교환
     *
     * @param message ICE Candidate 정보
     */
    @MessageMapping("/peer/candidate")
    public void candidate(@Payload SignalingMessage message) {
        log.info("Signaling Candidate: {}", message);
        messagingTemplate.convertAndSend("/sub/peer/room/" + message.getRoomId(), message);
    }
}

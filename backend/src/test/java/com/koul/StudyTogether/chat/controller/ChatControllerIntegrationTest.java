package com.koul.StudyTogether.chat.controller;

import com.koul.StudyTogether.chat.dto.ChatMessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private WebSocketStompClient stompClient;
    private BlockingQueue<ChatMessageDto> receivedMessages;

    @BeforeEach
    void setUp() {
        receivedMessages = new LinkedBlockingQueue<>();

        List<Transport> transports = List.of(new WebSocketTransport(new StandardWebSocketClient()));
        SockJsClient sockJsClient = new SockJsClient(transports);

        stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    @DisplayName("WebSocket 연결 및 메시지 송수신 테스트")
    void websocketConnectionTest() throws Exception {
        // given
        String url = "ws://localhost:" + port + "/ws-stomp";

        StompSession session = stompClient
                .connectAsync(url, new StompSessionHandlerAdapter() {})
                .get(3, TimeUnit.SECONDS);

        // 구독
        session.subscribe("/sub/chat/room/1", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDto.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                receivedMessages.add((ChatMessageDto) payload);
            }
        });

        // when - 메시지 전송
        ChatMessageDto message = new ChatMessageDto();
        message.setType(ChatMessageDto.MessageType.TALK);
        message.setRoomId("1");
        message.setSender("testUser");
        message.setMemberId(1L);
        message.setMessage("테스트 메시지");

        session.send("/pub/chat/message", message);

        // then
        ChatMessageDto received = receivedMessages.poll(3, TimeUnit.SECONDS);
        assertThat(received).isNotNull();
        assertThat(received.getMessage()).isEqualTo("테스트 메시지");
    }
}
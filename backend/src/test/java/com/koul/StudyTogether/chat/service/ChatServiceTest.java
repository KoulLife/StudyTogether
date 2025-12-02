package com.koul.StudyTogether.chat.service;

import com.koul.StudyTogether.chat.domain.ChatChannel;
import com.koul.StudyTogether.chat.domain.ChatMessage;
import com.koul.StudyTogether.chat.dto.ChatMessageDto;
import com.koul.StudyTogether.chat.repository.ChatChannelRepository;
import com.koul.StudyTogether.chat.repository.ChatMessageRepository;
import com.koul.StudyTogether.member.domain.Member;
import com.koul.StudyTogether.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatChannelRepository chatChannelRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private ChatService chatService;

    @Test
    @DisplayName("채팅 메시지 저장 성공")
    void saveMessage_Success() {
        // given
        ChatMessageDto dto = new ChatMessageDto();
        dto.setRoomId("1");
        dto.setMemberId(1L);
        dto.setMessage("안녕하세요");

        ChatChannel chatChannel = ChatChannel.builder().build();
        Member member = Member.builder().build();
        ChatMessage savedMessage = ChatMessage.builder()
                .chatChannel(chatChannel)
                .member(member)
                .content("안녕하세요")
                .build();

        given(chatChannelRepository.findById(1L)).willReturn(Optional.of(chatChannel));
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(chatMessageRepository.save(any(ChatMessage.class))).willReturn(savedMessage);

        // when
        ChatMessage result = chatService.saveMessage(dto);

        // then
        assertThat(result.getContent()).isEqualTo("안녕하세요");
        verify(chatMessageRepository).save(any(ChatMessage.class));
    }

    @Test
    @DisplayName("존재하지 않는 채팅방으로 메시지 저장 시 예외 발생")
    void saveMessage_ChatChannelNotFound() {
        // given
        ChatMessageDto dto = new ChatMessageDto();
        dto.setRoomId("999");
        dto.setMemberId(1L);

        given(chatChannelRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> chatService.saveMessage(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 채팅방입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 메시지 저장 시 예외 발생")
    void saveMessage_MemberNotFound() {
        // given
        ChatMessageDto dto = new ChatMessageDto();
        dto.setRoomId("1");
        dto.setMemberId(999L);

        ChatChannel chatChannel = ChatChannel.builder().build();
        given(chatChannelRepository.findById(1L)).willReturn(Optional.of(chatChannel));
        given(memberRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> chatService.saveMessage(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 회원입니다.");
    }
}
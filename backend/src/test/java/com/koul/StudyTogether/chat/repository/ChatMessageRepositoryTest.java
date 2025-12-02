package com.koul.StudyTogether.chat.repository;

import com.koul.StudyTogether.chat.domain.ChatChannel;
import com.koul.StudyTogether.chat.domain.ChatMessage;
import com.koul.StudyTogether.member.domain.Member;
import com.koul.StudyTogether.member.domain.Role;
import com.koul.StudyTogether.studyRoom.domain.StudyRoom;
import com.koul.StudyTogether.studySession.domain.StudySession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
class ChatMessageRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatChannelRepository chatChannelRepository;

    @Test
    @DisplayName("채팅 채널별 메시지 조회 테스트")
    void findByChatChannelIdOrderByCreatedAtDesc() {
        // given
        Member member = createMember("testUser");
        StudyRoom studyRoom = createStudyRoom("테스트 스터디룸", "P@ssw0rd");
        StudySession studySession = createStudySession(member, studyRoom);
        ChatChannel chatChannel = createChatChannel(studySession);

        ChatMessage message1 = createChatMessage(chatChannel, member, "첫 번째 메시지");
        ChatMessage message2 = createChatMessage(chatChannel, member, "두 번째 메시지");

        em.flush();
        em.clear();

        // when
        Page<ChatMessage> result = chatMessageRepository
                .findByChatChannelIdOrderByCreatedAtDesc(
                        chatChannel.getId(),
                        PageRequest.of(0, 10)
                );

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getContent()).isEqualTo("두 번째 메시지");
    }

    @Test
    @DisplayName("채팅 메시지 개수 조회 테스트")
    void countByChatChannelId() {
        // given
        Member member = createMember("testUser");
        StudyRoom studyRoom = createStudyRoom("테스트 스터디룸", "P@ssw0rd");
        StudySession studySession = createStudySession(member, studyRoom);
        ChatChannel chatChannel = createChatChannel(studySession);

        createChatMessage(chatChannel, member, "메시지1");
        createChatMessage(chatChannel, member, "메시지2");
        createChatMessage(chatChannel, member, "메시지3");

        em.flush();

        // when
        long count = chatMessageRepository.countByChatChannelId(chatChannel.getId());

        // then
        assertThat(count).isEqualTo(3);
    }

    // ============ 헬퍼 메서드들 ============

    private Member createMember(String username) {
        Member member = Member.builder()
                .username(username)
                .name(username)
                .email(username + "@test.com")
                .password("password123")
                .role(Role.USER)
                .build();
        return em.persist(member);
    }

    private StudyRoom createStudyRoom(String name, String password) {
        StudyRoom studyRoom = StudyRoom.builder()
                .name(name)
                .password(password)
                .build();
        return em.persist(studyRoom);
    }

    private StudySession createStudySession(Member member, StudyRoom studyRoom) {
        StudySession studySession = StudySession.builder()
                .member(member)
                .studyRoom(studyRoom)
                .build();
        return em.persist(studySession);
    }

    private ChatChannel createChatChannel(StudySession studySession) {
        ChatChannel chatChannel = ChatChannel.builder()
                .studySession(studySession)
                .build();
        return em.persist(chatChannel);
    }

    private ChatMessage createChatMessage(ChatChannel chatChannel, Member member, String content) {
        ChatMessage message = ChatMessage.builder()
                .chatChannel(chatChannel)
                .member(member)
                .content(content)
                .build();
        return em.persist(message);
    }
}
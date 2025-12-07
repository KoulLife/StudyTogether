package com.koul.StudyTogether.studySession.service;

import com.koul.StudyTogether.member.domain.Member;
import com.koul.StudyTogether.member.repository.MemberRepository;
import com.koul.StudyTogether.studyRoom.domain.StudyRoom;
import com.koul.StudyTogether.studyRoom.repository.StudyRoomRepository;
import com.koul.StudyTogether.studySession.domain.StudySession;
import com.koul.StudyTogether.studySession.dto.StudySessionRequest;
import com.koul.StudyTogether.studySession.dto.StudySessionResponse;
import com.koul.StudyTogether.studySession.repository.StudySessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 학습 세션 서비스
 *
 * 회원의 학습 시작/종료 시간을 기록하고, 누적 학습 시간을 계산
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudySessionService {

        private final StudySessionRepository studySessionRepository;
        private final MemberRepository memberRepository;
        private final StudyRoomRepository studyRoomRepository;

        /**
         * 학습 시작을 기록
         *
         * 이미 진행 중인 세션이 있다면 예외를 발생
         *
         * @param request 세션 시작 요청 (회원ID, 방ID)
         * @return 생성된 세션 정보
         */
        @Transactional
        public StudySessionResponse startStudy(StudySessionRequest request) {
                Member member = memberRepository.findById(request.getMemberId())
                                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
                StudyRoom studyRoom = studyRoomRepository.findById(request.getRoomId())
                                .orElseThrow(() -> new IllegalArgumentException("StudyRoom not found"));

                if (studySessionRepository
                                .findFirstByMemberIdAndStudyRoomIdAndEndedAtIsNull(request.getMemberId(),
                                                request.getRoomId())
                                .isPresent()) {
                        throw new IllegalStateException("Study session already exists");
                }

                StudySession studySession = StudySession.builder()
                                .member(member)
                                .studyRoom(studyRoom)
                                .build();

                return StudySessionResponse.from(studySessionRepository.save(studySession));
        }

        /**
         * 학습을 종료하고 시간을 저장
         *
         * 종료 시간(endedAt)을 업데이트하고, 시작 시간과의 차이를 분 단위로 계산하여 저장
         *
         * @param request 세션 종료 요청
         * @return 종료된 세션 정보 (학습 시간 포함)
         */
        @Transactional
        public StudySessionResponse endStudy(StudySessionRequest request) {
                StudySession studySession = studySessionRepository
                                .findFirstByMemberIdAndStudyRoomIdAndEndedAtIsNull(request.getMemberId(),
                                                request.getRoomId())
                                .orElseThrow(() -> new IllegalArgumentException("Active study session not found"));

                studySession.endSession();
                return StudySessionResponse.from(studySession);
        }
}

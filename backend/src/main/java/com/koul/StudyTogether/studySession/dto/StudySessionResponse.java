package com.koul.StudyTogether.studySession.dto;

import com.koul.StudyTogether.studySession.domain.StudySession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudySessionResponse {
    private Long id;
    private Long memberId;
    private Long roomId;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Integer durationMinutes;

    public static StudySessionResponse from(StudySession session) {
        return StudySessionResponse.builder()
                .id(session.getId())
                .memberId(session.getMember().getId())
                .roomId(session.getStudyRoom().getId())
                .startedAt(session.getStartedAt())
                .endedAt(session.getEndedAt())
                .durationMinutes(session.getDurationMinutes())
                .build();
    }
}

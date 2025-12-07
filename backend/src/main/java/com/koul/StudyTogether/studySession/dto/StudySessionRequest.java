package com.koul.StudyTogether.studySession.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudySessionRequest {
    private Long memberId;
    private Long roomId;
}

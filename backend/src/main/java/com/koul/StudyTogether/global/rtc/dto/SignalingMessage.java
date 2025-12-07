package com.koul.StudyTogether.global.rtc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SignalingMessage {
    private String type; // offer, answer, candidate, join, leave
    private String roomId;
    private String sender; // sender sessionId or username
    private Object payload; // SDP or IceCandidate
}

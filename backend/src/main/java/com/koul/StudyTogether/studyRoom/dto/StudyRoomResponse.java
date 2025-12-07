package com.koul.StudyTogether.studyRoom.dto;

import com.koul.StudyTogether.studyRoom.domain.StudyRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyRoomResponse {
    private Long id;
    private String name;
    private Integer memberCount;
    private boolean isPrivate;

    public static StudyRoomResponse from(StudyRoom studyRoom) {
        return StudyRoomResponse.builder()
                .id(studyRoom.getId())
                .name(studyRoom.getName())
                .memberCount(studyRoom.getMemberCount())
                .isPrivate(studyRoom.getPassword() != null && !studyRoom.getPassword().isEmpty())
                .build();
    }
}

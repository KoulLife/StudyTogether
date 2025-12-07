package com.koul.StudyTogether.studyRoom.service;

import com.koul.StudyTogether.studyRoom.domain.StudyRoom;
import com.koul.StudyTogether.studyRoom.dto.StudyRoomRequest;
import com.koul.StudyTogether.studyRoom.dto.StudyRoomResponse;
import com.koul.StudyTogether.studyRoom.repository.StudyRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 스터디룸 비즈니스 로직 처리 서비스
 *
 * 스터디룸 생성, 조회, 입장 가능 여부 확인(비밀번호 검증) 등의 핵심 기능을 수행
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyRoomService {

    private final StudyRoomRepository studyRoomRepository;

    /**
     * 스터디룸을 생성하고 저장
     *
     * @param request 방 생성 요청 DTO
     * @return 생성된 방의 응답 DTO
     */
    @Transactional
    public StudyRoomResponse createRoom(StudyRoomRequest request) {
        StudyRoom studyRoom = StudyRoom.builder()
                .name(request.getName())
                .password(request.getPassword())
                .build();

        StudyRoom savedRoom = studyRoomRepository.save(studyRoom);
        return StudyRoomResponse.from(savedRoom);
    }

    /**
     * 전체 스터디룸 목록을 조회
     *
     * @return 스터디룸 응답 DTO 리스트
     */
    public List<StudyRoomResponse> getAllRooms() {
        // TODO: Pagination support
        return studyRoomRepository.findAll().stream()
                .map(StudyRoomResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * ID로 스터디룸 상세 정보를 조회
     *
     * @param roomId 조회할 방 ID
     * @return 스터디룸 응답 DTO
     * @throws IllegalArgumentException 방이 존재하지 않을 경우 발생
     */
    public StudyRoomResponse getRoom(Long roomId) {
        StudyRoom studyRoom = studyRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("StudyRoom not found with id: " + roomId));
        return StudyRoomResponse.from(studyRoom);
    }

    /**
     * 스터디룸 입장을 처리하고 비밀번호를 검증
     *
     * @param roomId   입장할 방 ID
     * @param password 사용자가 입력한 비밀번호 (공개방일 경우 null 가능)
     * @throws IllegalArgumentException 방이 없거나 비밀번호가 불일치할 경우 발생
     */
    @Transactional
    public void joinRoom(Long roomId, String password) {
        StudyRoom studyRoom = studyRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("StudyRoom not found with id: " + roomId));

        if (studyRoom.getPassword() != null && !studyRoom.getPassword().isEmpty()) {
            if (!studyRoom.getPassword().equals(password)) {
                throw new IllegalArgumentException("Invalid password");
            }
        }
    }
}

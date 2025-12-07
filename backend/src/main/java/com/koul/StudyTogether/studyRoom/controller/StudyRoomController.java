package com.koul.StudyTogether.studyRoom.controller;

import com.koul.StudyTogether.studyRoom.dto.StudyRoomRequest;
import com.koul.StudyTogether.studyRoom.dto.StudyRoomResponse;
import com.koul.StudyTogether.studyRoom.service.StudyRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 스터디룸 관리 API 컨트롤러
 *
 * 스터디룸의 생성, 조회, 입장 등 방 관리와 관련된 HTTP 요청을 처리
 */
@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class StudyRoomController {

    private final StudyRoomService studyRoomService;

    /**
     * 새로운 스터디룸을 생성
     *
     * @param request 스터디룸 생성 요청 정보 (방 이름, 비밀번호 등)
     * @return 생성된 스터디룸 정보
     */
    @PostMapping
    public ResponseEntity<StudyRoomResponse> createRoom(@RequestBody StudyRoomRequest request) {
        return ResponseEntity.ok(studyRoomService.createRoom(request));
    }

    /**
     * 개설된 모든 스터디룸 목록을 조회
     * TODO: 추후 페이징(Pagination) 처리가 필요
     *
     * @return 스터디룸 목록
     */
    @GetMapping
    public ResponseEntity<List<StudyRoomResponse>> getAllRooms() {
        return ResponseEntity.ok(studyRoomService.getAllRooms());
    }

    /**
     * 특정 스터디룸의 상세 정보를 조회
     *
     * @param id 스터디룸 ID
     * @return 스터디룸 상세 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudyRoomResponse> getRoom(@PathVariable Long id) {
        return ResponseEntity.ok(studyRoomService.getRoom(id));
    }

    /**
     * 스터디룸에 입장을 요청
     * 비밀방일 경우 비밀번호 검증을 수행
     *
     * @param id      스터디룸 ID
     * @param request 입장 요청 정보 (비밀번호 포함)
     * @return 성공 시 200 OK
     */
    @PostMapping("/{id}/join")
    public ResponseEntity<Void> joinRoom(@PathVariable Long id,
            @RequestBody(required = false) StudyRoomRequest request) {
        String password = (request != null) ? request.getPassword() : null;
        studyRoomService.joinRoom(id, password);
        return ResponseEntity.ok().build();
    }
}

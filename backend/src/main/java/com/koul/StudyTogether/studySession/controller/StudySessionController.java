package com.koul.StudyTogether.studySession.controller;

import com.koul.StudyTogether.studySession.dto.StudySessionRequest;
import com.koul.StudyTogether.studySession.dto.StudySessionResponse;
import com.koul.StudyTogether.studySession.service.StudySessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/study")
@RequiredArgsConstructor
public class StudySessionController {

    private final StudySessionService studySessionService;

    @PostMapping("/start")
    public ResponseEntity<StudySessionResponse> startStudy(@RequestBody StudySessionRequest request) {
        return ResponseEntity.ok(studySessionService.startStudy(request));
    }

    @PostMapping("/end")
    public ResponseEntity<StudySessionResponse> endStudy(@RequestBody StudySessionRequest request) {
        return ResponseEntity.ok(studySessionService.endStudy(request));
    }
}

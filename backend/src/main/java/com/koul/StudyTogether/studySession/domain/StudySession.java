package com.koul.StudyTogether.studySession.domain;

import java.time.LocalDateTime;

import com.koul.StudyTogether.global.common.BaseEntity;
import com.koul.StudyTogether.member.domain.Member;
import com.koul.StudyTogether.studyRoom.domain.StudyRoom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "study_session")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudySession extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "study_session_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_room_id", nullable = false)
	private StudyRoom studyRoom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "started_at", nullable = false)
	private LocalDateTime startedAt;

	@Column(name = "ended_at")
	private LocalDateTime endedAt;

	@Column(name = "duration_minutes")
	private Integer durationMinutes;

	@Builder
	public StudySession(StudyRoom studyRoom, Member member) {
		this.studyRoom = studyRoom;
		this.member = member;
		this.startedAt = LocalDateTime.now();
	}

	public void endSession() {
		this.endedAt = LocalDateTime.now();
		this.durationMinutes = (int) java.time.Duration.between(this.startedAt, this.endedAt).toMinutes();
	}
}

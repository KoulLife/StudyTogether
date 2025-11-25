package com.koul.StudyTogether.studyRoom.domain;

import java.time.LocalDateTime;

import com.koul.StudyTogether.global.common.BaseEntity;
import com.koul.StudyTogether.member.domain.Member;

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
@Table(name = "member_study_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberStudyRoom extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_study_room_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_room_id", nullable = false)
	private StudyRoom studyRoom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "is_active")
	private Boolean isActive = true;

	@Column(name = "joined_at", nullable = false)
	private LocalDateTime joinedAt;

	@Column(name = "left_at")
	private LocalDateTime leftAt;

	@Builder
	public MemberStudyRoom(StudyRoom studyRoom, Member member) {
		this.studyRoom = studyRoom;
		this.member = member;
		this.isActive = true;
		this.joinedAt = LocalDateTime.now();
	}

	// 방을 떠남
	public void leave() {
		this.isActive = false;
		this.leftAt = LocalDateTime.now();
	}

	// 방을 다시 들어옴
	public void rejoin() {
		this.isActive = true;
		this.leftAt = null;
		this.joinedAt = LocalDateTime.now();
	}
}

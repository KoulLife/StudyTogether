package com.koul.StudyTogether.studyRoom.domain;

import com.koul.StudyTogether.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "study_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyRoom extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "study_room_id")
	private Long id;

	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Column(name = "member_count")
	private Integer memberCount = 0;

	@Column(name = "password", length = 255)
	private String password;  // NULL이면 공개방

	@Builder
	public StudyRoom(String name, String password) {
		this.name = name;
		this.password = password;
		this.memberCount = 0;
	}
}

package com.koul.StudyTogether.chat.domain;

import com.koul.StudyTogether.global.common.BaseEntity;
import com.koul.StudyTogether.studySession.domain.StudySession;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_channel")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatChannel extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_channel_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_session_id", nullable = false)
	private StudySession studySession;

	@Builder
	public ChatChannel(StudySession studySession) {
		this.studySession = studySession;
	}
}

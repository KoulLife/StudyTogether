package com.koul.StudyTogether.member.domain;

import com.koul.StudyTogether.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Column(name = "email", nullable = false, length = 50, unique = true)
	private String email;

	@Column(name = "username", nullable = false, length = 20, unique = true)
	private String username;

	@Column(name = "password", nullable = false, length = 255)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, length = 20)
	private Role role;

	@Builder
	public Member(String name, String email, String username,
		String password, Role role) {
		this.name = name;
		this.email = email;
		this.username = username;
		this.password = password;
		this.role = role;
	}
}

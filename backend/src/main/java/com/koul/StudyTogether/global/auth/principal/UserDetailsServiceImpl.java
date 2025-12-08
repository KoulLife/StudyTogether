package com.koul.StudyTogether.global.auth.principal;

import com.koul.StudyTogether.member.domain.Member;
import com.koul.StudyTogether.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return memberRepository.findByEmail(email)
				.map(this::createUserDetails)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
	}

	private UserDetails createUserDetails(Member member) {
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().toString());

		return new User(
				member.getEmail(), // principal for authentication (email)
				member.getPassword(),
				Collections.singleton(grantedAuthority));
	}
}

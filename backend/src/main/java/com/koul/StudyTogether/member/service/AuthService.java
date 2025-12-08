package com.koul.StudyTogether.member.service;

import com.koul.StudyTogether.global.auth.jwt.JwtTokenProvider;
import com.koul.StudyTogether.member.domain.Member;
import com.koul.StudyTogether.member.domain.Role;
import com.koul.StudyTogether.member.dto.LoginRequest;
import com.koul.StudyTogether.member.dto.SignUpRequest;
import com.koul.StudyTogether.member.dto.TokenResponse;
import com.koul.StudyTogether.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public void signUp(SignUpRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 유저네임입니다.");
        }

        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .username(request.getUsername())
                .role(Role.ROLE_USER)
                .build();

        memberRepository.save(member);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());

        // authenticate 메소드가 실행이 될 때 UserDetailsServiceImpl의 loadUserByUsername 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

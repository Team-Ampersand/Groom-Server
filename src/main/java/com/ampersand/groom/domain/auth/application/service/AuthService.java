package com.ampersand.groom.domain.auth.application.service;

import com.ampersand.groom.domain.auth.application.port.AuthPort;
import com.ampersand.groom.domain.auth.expection.*;
import com.ampersand.groom.domain.auth.domain.JwtToken;
import com.ampersand.groom.domain.auth.presentation.data.Request.SignupRequest;
import com.ampersand.groom.domain.member.persistence.entity.MemberJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthPort authPort;

    @Value("${spring.jwt.token.access-expiration}")
    private long accessTokenExpiration;

    @Value("${spring.jwt.token.refresh-expiration}")
    private long refreshTokenExpiration;


    private static final int PASSWORD_MAX_LENGTH = 30;
    private static final int EMAIL_MAX_LENGTH = 16;

    public JwtToken signIn(String email, String password) {
        if (email == null || password == null || email.isEmpty() || password.isEmpty() || email.length() > EMAIL_MAX_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
            throw new EmailOrPasswordEmptyException();
        }

        MemberJpaEntity user = authPort.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException());

        if(!user.getIsAvailable()) {
            throw new UserForbiddenException();
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordInvalidException();
        }

        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken(email);

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiration(Instant.now().plusMillis(accessTokenExpiration))
                .refreshTokenExpiration(Instant.now().plusMillis(refreshTokenExpiration))
                .role(user.getRole())
                .build();
    }

    public JwtToken refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RefreshTokenRequestFormatInvalidException();
        }

        if (!jwtService.validateToken(refreshToken)) {
            throw new RefreshTokenExpiredOrInvalidException();
        }

        String email = jwtService.getEmailFromToken(refreshToken);
        MemberJpaEntity user = authPort.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException());

        String newAccessToken = jwtService.createAccessToken(email);
        String newRefreshToken = jwtService.createRefreshToken(email);

        return JwtToken.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .accessTokenExpiration(Instant.now().plusMillis(accessTokenExpiration))
                .refreshTokenExpiration(Instant.now().plusMillis(refreshTokenExpiration))
                .role(user.getRole())
                .build();
    }

    public void signup(SignupRequest request) {
        authPort.findByEmail(request.getEmail())
                .ifPresent(emailVerification -> {
                    throw new UserExistException();
                });

        MemberJpaEntity newUser = MemberJpaEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .generation(1)
                .isAvailable(true)
                .build();

        authPort.save(newUser);
    }
}

package com.ampersand.groom.domain.auth.application.service;

import com.ampersand.groom.domain.auth.application.port.AuthPort;
import com.ampersand.groom.domain.auth.application.port.AuthenticationPersistencePort;
import com.ampersand.groom.domain.auth.domain.JwtToken;
import com.ampersand.groom.domain.auth.exception.*;
import com.ampersand.groom.domain.auth.presentation.data.request.SignupRequest;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.domain.member.persistence.entity.MemberJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthPort authPort;
    private final AuthenticationPersistencePort authenticationPersistencePort;

    @Value("${spring.jwt.token.access-expiration}")
    private long accessTokenExpiration;

    @Value("${spring.jwt.token.refresh-expiration}")
    private long refreshTokenExpiration;

    public JwtToken signIn(String email, String password) {
        MemberJpaEntity user = findUserByEmail(email);
        validateUserStatus(user);
        validatePassword(password, user.getPassword());
        return generateJwtToken(email, user.getRole());
    }

    public JwtToken refreshToken(String refreshToken) {
        validateRefreshToken(refreshToken);
        String email = jwtService.getEmailFromToken(refreshToken);
        validateToken(email, refreshToken);
        MemberJpaEntity user = findUserByEmail(email);
        return generateJwtToken(email, user.getRole());
    }

    public void signup(SignupRequest request) {
        checkUserExists(request.getEmail());
        if(!authenticationPersistencePort.existsAuthenticationByEmail(request.getEmail())
                || !authenticationPersistencePort.findAuthenticationByEmail(request.getEmail()).getVerified()) {
            throw new UserForbiddenException();
        }
        MemberJpaEntity newUser = createNewUser(request, calculateGenerationFromEmail(request.getEmail()));
        authPort.save(newUser);
    }

    private MemberJpaEntity findUserByEmail(String email) {
        return authPort.findMembersByCriteria(email)
                .orElseThrow(UserNotFoundException::new);
    }

    private void validateUserStatus(MemberJpaEntity user) {
        if (!user.getIsAvailable()) {
            throw new UserForbiddenException();
        }
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new PasswordInvalidException();
        }
    }

    private JwtToken generateJwtToken(String email, MemberRole role) {
        String accessToken = jwtService.createAccessToken(email, role);
        String refreshToken = jwtService.createRefreshToken(email, role);
        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiration(Instant.now().plusMillis(accessTokenExpiration))
                .refreshTokenExpiration(Instant.now().plusMillis(refreshTokenExpiration))
                .role(role)
                .build();
    }

    private void validateRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RefreshTokenRequestFormatInvalidException();
        }
    }

    private void validateToken(String email, String refreshToken) {
        if (!jwtService.refreshToken(email, refreshToken) || !jwtService.validateToken(refreshToken)) {
            throw new RefreshTokenExpiredOrInvalidException();
        }
    }

    private void checkUserExists(String email) {
        authPort.findMembersByCriteria(email)
                .ifPresent(user -> {
                    throw new UserExistException();
                });
    }

    private int calculateGenerationFromEmail(String email) {
        try {
            Matcher matcher = Pattern.compile("\\d{2}").matcher(email);
            if (!matcher.find()) {
                throw new EmailFormatInvalidException();
            }
            int admissionYear = Integer.parseInt(matcher.group()) + 2000;
            return (admissionYear - 2017) + 1;
        } catch (NumberFormatException e) {
            throw new EmailFormatInvalidException();
        }
    }

    private MemberJpaEntity createNewUser(SignupRequest request, int generation) {
        return MemberJpaEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .generation(generation)
                .isAvailable(true)
                .role(MemberRole.ROLE_STUDENT)
                .build();
    }
}
package com.ampersand.groom.domain.member.application.usecase;

import com.ampersand.groom.domain.auth.application.port.AuthenticationPersistencePort;
import com.ampersand.groom.domain.auth.domain.Authentication;
import com.ampersand.groom.domain.auth.exception.PasswordInvalidException;
import com.ampersand.groom.domain.auth.exception.UserForbiddenException;
import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdatePasswordUseCase 클래스의")
class UpdatePasswordUseCaseTest {

    @Mock
    private AuthenticationPersistencePort authenticationPersistencePort;

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UpdatePasswordUseCase updatePasswordUseCase;

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("사용자 인증 정보가 없거나 인증되지 않은 경우")
        class Context_with_unverified_user {

            @Test
            @DisplayName("UserForbiddenException을 던진다.")
            void it_throws_UserForbiddenException() {
                // Given
                Long memberId = 1L;
                String email = "s00001@email.com";
                Member member = Member.builder()
                        .id(memberId)
                        .email(email)
                        .build();
                when(memberPersistencePort.findMemberById(memberId)).thenReturn(member);
                when(authenticationPersistencePort.existsAuthenticationByEmail(email)).thenReturn(true);
                when(authenticationPersistencePort.findAuthenticationByEmail(email))
                        .thenReturn(Authentication.builder().email(email).verified(false).build());

                // When & Then
                assertThrows(UserForbiddenException.class, () ->
                        updatePasswordUseCase.execute(memberId, "oldPass", "newPass"));
            }
        }

        @Nested
        @DisplayName("현재 비밀번호가 올바르지 않은 경우")
        class Context_with_invalid_password {

            @Test
            @DisplayName("PasswordInvalidException을 던진다.")
            void it_throws_PasswordInvalidException() {
                // Given
                Long memberId = 1L;
                String email = "s00001@email.com";
                String encodedPassword = "encodedPassword";
                Member member = Member.builder()
                        .id(memberId)
                        .email(email)
                        .password(encodedPassword)
                        .build();
                when(memberPersistencePort.findMemberById(memberId)).thenReturn(member);
                when(authenticationPersistencePort.existsAuthenticationByEmail(email)).thenReturn(true);
                when(authenticationPersistencePort.findAuthenticationByEmail(email))
                        .thenReturn(Authentication.builder().email(email).verified(true).build());
                when(passwordEncoder.matches("oldPass", encodedPassword)).thenReturn(false);

                // When & Then
                assertThrows(PasswordInvalidException.class, () ->
                        updatePasswordUseCase.execute(memberId, "oldPass", "newPass"));
            }
        }

        @Nested
        @DisplayName("모든 조건이 충족되는 경우")
        class Context_with_valid_password_update {

            @Test
            @DisplayName("비밀번호를 성공적으로 변경한다.")
            void it_updates_password_successfully() {
                // Given
                Long memberId = 1L;
                String email = "s00001@email.com";
                String encodedPassword = "encodedOld";
                String newEncodedPassword = "encodedNew";
                Member member = Member.builder()
                        .id(memberId)
                        .email(email)
                        .password(encodedPassword)
                        .build();
                when(memberPersistencePort.findMemberById(memberId)).thenReturn(member);
                when(authenticationPersistencePort.existsAuthenticationByEmail(email)).thenReturn(true);
                when(authenticationPersistencePort.findAuthenticationByEmail(email))
                        .thenReturn(Authentication.builder().email(email).verified(true).build());
                when(passwordEncoder.matches("oldPass", encodedPassword)).thenReturn(true);
                when(passwordEncoder.encode("newPass")).thenReturn(newEncodedPassword);

                // When
                updatePasswordUseCase.execute(memberId, "oldPass", "newPass");

                // Then
                verify(memberPersistencePort).updateMemberPassword(memberId, newEncodedPassword);
            }
        }
    }
}
package com.ampersand.groom.domain.auth.application.usecase;

import com.ampersand.groom.domain.auth.application.port.AuthenticationPersistencePort;
import com.ampersand.groom.domain.auth.domain.Authentication;
import com.ampersand.groom.domain.auth.exception.EmailFormatInvalidException;
import com.ampersand.groom.domain.auth.exception.UserExistException;
import com.ampersand.groom.domain.auth.exception.UserForbiddenException;
import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("회원가입 UseCase 클래스의")
class SignUpUseCaseTest {

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @Mock
    private AuthenticationPersistencePort authenticationPersistencePort;

    @InjectMocks
    private SignUpUseCase signUpUseCase;

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("정상적인 회원가입 정보가 주어졌을 때")
        class Context_with_valid_signup_info {

            @Test
            @DisplayName("회원가입을 성공적으로 수행한다.")
            void it_signs_up_user() {
                // Given
                String email = "s24001@gsm.hs.kr";
                String password = "password123";
                String encodedPassword = "encodedPassword";
                String name = "홍길동";
                when(authenticationPersistencePort.findAuthenticationByEmail(email))
                        .thenReturn(Authentication.builder().email(email).verified(true).build());
                when(memberPersistencePort.existsMemberByEmail(email)).thenReturn(false);
                when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

                // When
                signUpUseCase.execute(email, password, name);

                // Then
                verify(memberPersistencePort).saveMember(argThat(member ->
                        member.getEmail().equals(email)
                                && member.getName().equals(name)
                                && member.getPassword().equals(encodedPassword)
                                && member.getRole().equals(MemberRole.ROLE_STUDENT)
                                && member.getGeneration() == 8
                                && member.getIsAvailable()
                ));
            }
        }

        @Nested
        @DisplayName("이메일 인증이 안 된 경우")
        class Context_with_unverified_authentication {

            @Test
            @DisplayName("UserForbiddenException을 던진다.")
            void it_throws_UserForbiddenException() {
                // Given
                String email = "s24001@gsm.hs.kr";
                when(authenticationPersistencePort.findAuthenticationByEmail(email))
                        .thenReturn(Authentication.builder().email(email).verified(false).build());

                // When & Then
                assertThrows(UserForbiddenException.class, () ->
                        signUpUseCase.execute(email, "password", "홍길동")
                );
            }
        }

        @Nested
        @DisplayName("이미 가입된 이메일인 경우")
        class Context_with_existing_email {

            @Test
            @DisplayName("UserExistException을 던진다.")
            void it_throws_UserExistException() {
                // Given
                String email = "s24001@gsm.hs.kr";
                when(authenticationPersistencePort.findAuthenticationByEmail(email))
                        .thenReturn(Authentication.builder().email(email).verified(true).build());
                when(memberPersistencePort.existsMemberByEmail(email)).thenReturn(true);

                // When & Then
                assertThrows(UserExistException.class, () ->
                        signUpUseCase.execute(email, "password", "홍길동")
                );
            }
        }

        @Nested
        @DisplayName("이메일 형식이 올바르지 않은 경우")
        class Context_with_invalid_email_format {

            @Test
            @DisplayName("EmailFormatInvalidException을 던진다.")
            void it_throws_EmailFormatInvalidException() {
                // Given
                String email = "invalidemail.com";
                when(authenticationPersistencePort.findAuthenticationByEmail(email))
                        .thenReturn(Authentication.builder().email(email).verified(true).build());
                when(memberPersistencePort.existsMemberByEmail(email)).thenReturn(false);

                // When & Then
                assertThrows(EmailFormatInvalidException.class, () ->
                        signUpUseCase.execute(email, "password", "홍길동")
                );
            }
        }
    }
}
package com.ampersand.groom.domain.auth.application.usecase;

import com.ampersand.groom.domain.auth.application.port.AuthenticationPersistencePort;
import com.ampersand.groom.domain.auth.domain.Authentication;
import com.ampersand.groom.domain.auth.exception.EmailFormatInvalidException;
import com.ampersand.groom.domain.auth.exception.UserExistException;
import com.ampersand.groom.domain.auth.exception.UserForbiddenException;
import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.global.annotation.usecase.UseCaseWithTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UseCaseWithTransaction
@RequiredArgsConstructor
public class SignUpUseCase {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberPersistencePort memberPersistencePort;
    private final AuthenticationPersistencePort authenticationPersistencePort;

    public void execute(String email, String password, String name) {
        Authentication authentication = authenticationPersistencePort.findAuthenticationByEmail(email);
        if (authentication == null || Boolean.FALSE.equals(authentication.getVerified())) {
            throw new UserForbiddenException();
        }
        if (memberPersistencePort.existsMemberByEmail(email)) {
            throw new UserExistException();
        }
        int generation = calculateGenerationFromEmail(email);
        Member newUser = Member.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .generation(generation)
                .isAvailable(true)
                .role(MemberRole.ROLE_STUDENT)
                .build();

        memberPersistencePort.saveMember(newUser);
    }

    private int calculateGenerationFromEmail(String email) {
        Matcher matcher = Pattern.compile("\\d{2}").matcher(email);
        if (matcher.find()) {
            try {
                int admissionYear = Integer.parseInt(matcher.group()) + 2000;
                return (admissionYear - 2017) + 1;
            } catch (NumberFormatException e) {
                throw new EmailFormatInvalidException();
            }
        }
        throw new EmailFormatInvalidException();
    }
}
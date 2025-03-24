package com.ampersand.groom.domain.auth.application.port;

import com.ampersand.groom.domain.auth.domain.Authentication;

public interface AuthenticationPersistencePort {

    Boolean existsAuthenticationByEmail(String email);

    Authentication findAuthenticationByEmail(String email);

    void saveAuthentication(Authentication authentication);
}
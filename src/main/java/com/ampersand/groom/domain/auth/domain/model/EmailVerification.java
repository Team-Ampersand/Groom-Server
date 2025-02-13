package com.ampersand.groom.domain.auth.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String code;
    private boolean isVerified;
    private LocalDateTime verificationDate;

    public EmailVerification(String email, String code) {
        this.email = email;
        this.code = code;
        this.isVerified = false;
        this.verificationDate = LocalDateTime.now().plusMinutes(5);
    }


}

package com.ampersand.groom.domain.auth.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "email")
@ToString
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private boolean isVerified;
    @Column(nullable = false)
    private LocalDateTime verificationDate;

    public EmailVerification(String email, String code) {
        this.email = email;
        this.code = code;
        this.isVerified = false;
        this.verificationDate = LocalDateTime.now().plusMinutes(5);
    }


    @Builder
    public EmailVerification(Long id, String email, String code, boolean isVerified, LocalDateTime verificationDate) {
        this.id = id;
        this.email = email;
        this.code = code;
        this.isVerified = isVerified;
        this.verificationDate = verificationDate;
    }


    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }
}

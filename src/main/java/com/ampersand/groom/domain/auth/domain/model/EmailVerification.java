package com.ampersand.groom.domain.auth.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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


}

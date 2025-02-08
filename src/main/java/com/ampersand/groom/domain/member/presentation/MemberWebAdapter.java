package com.ampersand.groom.domain.member.presentation;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.domain.member.presentation.data.response.GetCurrentMemberResponse;
import com.ampersand.groom.domain.member.presentation.data.response.GetMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberWebAdapter {

    // 의존성을 추가해주세요

    @GetMapping
    public ResponseEntity<List<GetMemberResponse>> getAllMembers() {
        return null;
    }

    @GetMapping("/search")
    public ResponseEntity<List<GetMemberResponse>> searchMembers(
            @RequestParam(value = "uuid", required = false) String uuid,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "generation", required = false) Integer generation,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "isAvailable", required = false) Boolean isAvailable,
            @RequestParam(value = "role", required = false) MemberRole role
    ) {
        return null;
    }

    @GetMapping("/current")
    public ResponseEntity<GetCurrentMemberResponse> getCurrentMember() {
        return null;
    }

    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateMemberPassword() {
        return null;
    }
}

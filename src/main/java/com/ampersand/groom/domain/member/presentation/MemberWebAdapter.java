package com.ampersand.groom.domain.member.presentation;

import com.ampersand.groom.domain.member.application.port.MemberApplicationPort;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.domain.member.presentation.data.request.UpdateMemberPasswordRequest;
import com.ampersand.groom.domain.member.presentation.data.response.GetCurrentMemberResponse;
import com.ampersand.groom.domain.member.presentation.data.response.GetMemberResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberWebAdapter {

    private final MemberApplicationPort memberApplicationPort;

    @GetMapping
    public ResponseEntity<List<GetMemberResponse>> getAllMembers() {
        return ResponseEntity.status(HttpStatus.OK).body(memberApplicationPort.findAllMembers());
    }

    @GetMapping("/search")
    public ResponseEntity<List<GetMemberResponse>> searchMembers(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "generation", required = false) Integer generation,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "isAvailable", required = false) Boolean isAvailable,
            @RequestParam(value = "role", required = false) MemberRole role
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(memberApplicationPort.findMembersByCriteria(id, name, generation, email, isAvailable, role));
    }

    @GetMapping("/current")
    public ResponseEntity<GetCurrentMemberResponse> getCurrentMember() {
        return ResponseEntity.status(HttpStatus.OK).body(memberApplicationPort.findCurrentMember());
    }

    @PatchMapping("/{email}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateMemberPassword(
            @PathVariable(value = "email") String email,
            @Valid @RequestBody UpdateMemberPasswordRequest request
    ) {
        memberApplicationPort.updatePassword(email, request.currentPassword(), request.newPassword());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
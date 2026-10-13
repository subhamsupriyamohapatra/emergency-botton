package com.example.Emergency_Alert_System_Backend.member.controller;

import com.example.Emergency_Alert_System_Backend.member.entity.Member;
import com.example.Emergency_Alert_System_Backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/rooms/{roomId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Member registerMember(
            @PathVariable Long roomId,
            @RequestBody Member member) {

        return memberService.registerMember(member, roomId);
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/rooms/{roomId}")
    public List<Member> getMembersByRoomId(
            @PathVariable Long roomId) {

        return memberService.getMembersByRoomId(roomId);
    }
}

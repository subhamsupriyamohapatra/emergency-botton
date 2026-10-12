package com.example.Emergency_Alert_System_Backend.member.service;

import com.example.Emergency_Alert_System_Backend.member.entity.Member;
import com.example.Emergency_Alert_System_Backend.member.repository.MemberRepository;
import com.example.Emergency_Alert_System_Backend.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public Member registerMember(Member member, Long roomId) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new IllegalArgumentException(
                    "Email is already registered"
            );


        }


    }

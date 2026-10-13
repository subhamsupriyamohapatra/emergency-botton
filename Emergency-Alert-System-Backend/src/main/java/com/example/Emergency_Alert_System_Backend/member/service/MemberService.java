package com.example.Emergency_Alert_System_Backend.member.service;

import com.example.Emergency_Alert_System_Backend.member.entity.Member;
import com.example.Emergency_Alert_System_Backend.member.repository.MemberRepository;
import com.example.Emergency_Alert_System_Backend.room.entity.Room;
import com.example.Emergency_Alert_System_Backend.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Room not found with id: " + roomId
                        )
                );

        member.setRoom(room);

        return memberRepository.save(member);
    }
    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Member> getMembersByRoomId(Long roomId) {

        if (!roomRepository.existsById(roomId)) {
            throw new IllegalArgumentException(
                    "Room not found with id: " + roomId
            );
        }

        return memberRepository.findByRoomId(roomId);
    }
}





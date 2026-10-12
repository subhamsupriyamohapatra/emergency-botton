package com.example.Emergency_Alert_System_Backend.member.repository;

import com.example.Emergency_Alert_System_Backend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member , Long> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Member> findByRoomId(Long roomId);
}

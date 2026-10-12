package com.example.Emergency_Alert_System_Backend.member.entity;

import com.example.Emergency_Alert_System_Backend.room.entity.Room;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@Data
@AllArgsConstructor
@NoArgsConstructor
 public class Member{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Member name is required")
    @Column(name = "member_name", nullable = false)
    private String memberName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Column(nullable = false, unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
}

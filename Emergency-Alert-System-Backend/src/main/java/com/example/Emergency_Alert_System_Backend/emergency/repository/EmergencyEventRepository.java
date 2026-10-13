package com.example.Emergency_Alert_System_Backend.emergency.repository;

import com.example.Emergency_Alert_System_Backend.emergency.entity.EmergencyEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EmergencyEventRepository extends JpaRepository<EmergencyEvent , Long> {
    List<EmergencyEvent> findByRoomId(Long roomId);
}

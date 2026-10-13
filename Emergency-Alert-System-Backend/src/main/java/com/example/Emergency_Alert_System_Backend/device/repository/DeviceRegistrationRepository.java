package com.example.Emergency_Alert_System_Backend.device.repository;

import com.example.Emergency_Alert_System_Backend.device.entity.DeviceRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRegistrationRepository
        extends JpaRepository<DeviceRegistration, Long> {

    Optional<DeviceRegistration> findByFcmToken(String fcmToken);

    List<DeviceRegistration> findByMemberId(Long memberId);

    List<DeviceRegistration> findByMemberRoomId(Long roomId);
}

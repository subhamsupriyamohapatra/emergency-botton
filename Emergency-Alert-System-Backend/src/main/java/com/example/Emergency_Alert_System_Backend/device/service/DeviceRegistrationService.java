package com.example.Emergency_Alert_System_Backend.device.service;

import com.example.Emergency_Alert_System_Backend.device.entity.DeviceRegistration;
import com.example.Emergency_Alert_System_Backend.device.repository.DeviceRegistrationRepository;
import com.example.Emergency_Alert_System_Backend.member.entity.Member;
import com.example.Emergency_Alert_System_Backend.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceRegistrationService {

    private final DeviceRegistrationRepository deviceRegistrationRepository;
    private final MemberRepository memberRepository;

    public DeviceRegistrationService(
            DeviceRegistrationRepository deviceRegistrationRepository,
            MemberRepository memberRepository) {
        this.deviceRegistrationRepository = deviceRegistrationRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public DeviceRegistration registerDevice(Long memberId, String fcmToken) {

        if (fcmToken == null || fcmToken.isBlank()) {
            throw new IllegalArgumentException("FCM token is required");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Member not found with id: " + memberId));

        DeviceRegistration existingDevice =
                deviceRegistrationRepository.findByFcmToken(fcmToken)
                        .orElse(null);

        if (existingDevice != null) {
            existingDevice.setMember(member);
            return deviceRegistrationRepository.save(existingDevice);
        }

        DeviceRegistration device = new DeviceRegistration();
        device.setMember(member);
        device.setFcmToken(fcmToken);

        return deviceRegistrationRepository.save(device);
    }

    @Transactional(readOnly = true)
    public List<DeviceRegistration> getAllDevices() {
        return deviceRegistrationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<DeviceRegistration> getDevicesByMemberId(Long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new IllegalArgumentException(
                    "Member not found with id: " + memberId);
        }

        return deviceRegistrationRepository.findByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public List<DeviceRegistration> getDevicesByRoomId(Long roomId) {
        return deviceRegistrationRepository.findByMemberRoomId(roomId);
    }
}

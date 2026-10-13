package com.example.Emergency_Alert_System_Backend.device.controller;

import com.example.Emergency_Alert_System_Backend.device.entity.DeviceRegistration;
import com.example.Emergency_Alert_System_Backend.device.service.DeviceRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor

public class DeviceRegistrationController {
    private final DeviceRegistrationService deviceRegistrationService;




    @PostMapping("/members/{memberId}")
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceRegistration registerDevice(
            @PathVariable Long memberId,
            @RequestBody Map<String, String> request) {

        return deviceRegistrationService.registerDevice(
                memberId, request.get("fcmToken"));
    }

    @GetMapping
    public List<DeviceRegistration> getAllDevices() {
        return deviceRegistrationService.getAllDevices();
    }

    @GetMapping("/members/{memberId}")
    public List<DeviceRegistration> getDevicesByMemberId(
            @PathVariable Long memberId) {

        return deviceRegistrationService.getDevicesByMemberId(memberId);
    }

    @GetMapping("/rooms/{roomId}")
    public List<DeviceRegistration> getDevicesByRoomId(
            @PathVariable Long roomId) {

        return deviceRegistrationService.getDevicesByRoomId(roomId);
    }
}


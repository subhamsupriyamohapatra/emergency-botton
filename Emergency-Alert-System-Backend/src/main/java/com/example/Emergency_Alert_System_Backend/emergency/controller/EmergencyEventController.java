package com.example.Emergency_Alert_System_Backend.emergency.controller;

import com.example.Emergency_Alert_System_Backend.emergency.dto.EmergencyEventResponse;
import com.example.Emergency_Alert_System_Backend.emergency.entity.EmergencyEvent;
import com.example.Emergency_Alert_System_Backend.emergency.service.EmergencyEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency-events")
@RequiredArgsConstructor
public class EmergencyEventController {
    private final EmergencyEventService emergencyEventService;

    @PostMapping("/rooms/{roomId}")
    @ResponseStatus(HttpStatus.CREATED)
    public EmergencyEventResponse createEmergencyEvent(
            @PathVariable Long roomId,
            @RequestParam Long memberId) {
        return emergencyEventService.createEmergencyEvent(roomId, memberId);
    }

    @GetMapping
    public List<EmergencyEvent> getAllEmergencyEvents() {
        return emergencyEventService.getAllEmergencyEvents();
    }

    @GetMapping("/rooms/{roomId}")
    public List<EmergencyEvent> getEmergencyEventsByRoomId(
            @PathVariable Long roomId) {
        return emergencyEventService.getEmergencyEventsByRoomId(roomId);
    }
}

package com.example.Emergency_Alert_System_Backend.emergency.service;

import com.example.Emergency_Alert_System_Backend.device.entity.DeviceRegistration;
import com.example.Emergency_Alert_System_Backend.device.repository.DeviceRegistrationRepository;
import com.example.Emergency_Alert_System_Backend.emergency.entity.EmergencyEvent;
import com.example.Emergency_Alert_System_Backend.emergency.repository.EmergencyEventRepository;
import com.example.Emergency_Alert_System_Backend.notification.service.FirebaseMessagingService;
import com.example.Emergency_Alert_System_Backend.room.entity.Room;
import com.example.Emergency_Alert_System_Backend.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmergencyEventService {
    private static final Logger log =
            LoggerFactory.getLogger(EmergencyEventService.class);
    private final EmergencyEventRepository emergencyEventRepository;
    private final RoomRepository roomRepository;
    private final DeviceRegistrationRepository deviceRegistrationRepository;
    private final FirebaseMessagingService firebaseMessagingService;
    @Transactional
    public EmergencyEvent createEmergencyEvent(Long roomId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Room not found with id: " + roomId));

        EmergencyEvent event = new EmergencyEvent();
        event.setRoom(room);
        event.setStatus("RECEIVED");

        // Save the emergency event first.
        EmergencyEvent savedEvent =
                emergencyEventRepository.save(event);

        // Find all devices registered by members of this room.
        List<DeviceRegistration> devices =
                deviceRegistrationRepository.findByMemberRoomId(roomId);

        // Send the notification to each registered device.
        for (DeviceRegistration device : devices) {
            try {
                firebaseMessagingService.sendNotification(
                        device.getFcmToken(),
                        "EMERGENCY ALERT",
                        "An emergency has been reported in room "
                                + room.getRoomNumber()
                );
            } catch (Exception e) {
                // One failed device must not prevent other notifications.
                log.error(
                        "Failed to notify device registration ID {}",
                        device.getId(),
                        e
                );
            }
        }

        return savedEvent;
    }

    @Transactional(readOnly = true)
    public List<EmergencyEvent> getAllEmergencyEvents() {
        return emergencyEventRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<EmergencyEvent> getEmergencyEventsByRoomId(Long roomId) {
        return emergencyEventRepository.findByRoomId(roomId);
    }
}

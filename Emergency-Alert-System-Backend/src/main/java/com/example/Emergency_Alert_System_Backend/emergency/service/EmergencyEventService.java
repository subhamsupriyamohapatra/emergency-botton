package com.example.Emergency_Alert_System_Backend.emergency.service;

import com.example.Emergency_Alert_System_Backend.device.entity.DeviceRegistration;
import com.example.Emergency_Alert_System_Backend.device.repository.DeviceRegistrationRepository;
import com.example.Emergency_Alert_System_Backend.emergency.dto.EmergencyEventResponse;
import com.example.Emergency_Alert_System_Backend.emergency.entity.EmergencyEvent;
import com.example.Emergency_Alert_System_Backend.emergency.repository.EmergencyEventRepository;
import com.example.Emergency_Alert_System_Backend.member.entity.Member;
import com.example.Emergency_Alert_System_Backend.member.repository.MemberRepository;
import com.example.Emergency_Alert_System_Backend.notification.service.FirebaseMessagingService;
import com.example.Emergency_Alert_System_Backend.room.entity.Room;
import com.example.Emergency_Alert_System_Backend.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmergencyEventService {
    private static final Logger log =
            LoggerFactory.getLogger(EmergencyEventService.class);
    private final EmergencyEventRepository emergencyEventRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final DeviceRegistrationRepository deviceRegistrationRepository;
    private final FirebaseMessagingService firebaseMessagingService;
    @Transactional
    public EmergencyEventResponse createEmergencyEvent(Long roomId, Long memberId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Room not found with id: " + roomId));

        // Identify the member who pressed the emergency button.
        Member triggeredBy = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Member not found with id: " + memberId));

        if (triggeredBy.getRoom() == null
                || !roomId.equals(triggeredBy.getRoom().getId())) {
            throw new IllegalArgumentException(
                    "Member " + memberId + " does not belong to room " + roomId);
        }

        EmergencyEvent event = new EmergencyEvent();
        event.setRoom(room);
        event.setTriggeredBy(triggeredBy);
        event.setStatus("RECEIVED");

        // Save the emergency event first.
        EmergencyEvent savedEvent =
                emergencyEventRepository.save(event);

        // Every member of the room is a recipient (except the triggerer).
        // Members without a registered device are still listed but cannot
        // receive a push.
        List<Member> members = memberRepository.findByRoomId(roomId);

        // Group the room's devices by member so each member can be notified
        // on all of their registered devices.
        List<DeviceRegistration> devices =
                deviceRegistrationRepository.findByMemberRoomId(roomId);

        Map<Long, List<DeviceRegistration>> devicesByMember = devices.stream()
                .collect(Collectors.groupingBy(
                        device -> device.getMember().getId()));

        List<EmergencyEventResponse.MemberInfo> recipients =
                new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        for (Member member : members) {
            // Do not notify the member who triggered the alert.
            if (member.getId().equals(memberId)) {
                continue;
            }

            recipients.add(new EmergencyEventResponse.MemberInfo(
                    member.getId(),
                    member.getMemberName(),
                    member.getEmail()
            ));

            for (DeviceRegistration device :
                    devicesByMember.getOrDefault(member.getId(), List.of())) {
                try {
                    firebaseMessagingService.sendNotification(
                            device.getFcmToken(),
                            "EMERGENCY ALERT",
                            "An emergency has been reported in room "
                                    + room.getRoomNumber()
                    );
                    successCount++;
                } catch (Exception e) {
                    failureCount++;
                    // One failed device must not prevent other notifications.
                    log.error(
                            "Failed to notify device registration ID {}",
                            device.getId(),
                            e
                    );
                }
            }
        }

        return new EmergencyEventResponse(
                savedEvent.getId(),
                savedEvent.getStatus(),
                savedEvent.getCreatedAt(),
                new EmergencyEventResponse.RoomInfo(
                        room.getId(),
                        room.getRoomNumber()
                ),
                new EmergencyEventResponse.MemberInfo(
                        triggeredBy.getId(),
                        triggeredBy.getMemberName(),
                        triggeredBy.getEmail()
                ),
                recipients,
                successCount,
                failureCount
        );
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

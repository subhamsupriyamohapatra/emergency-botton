package com.example.Emergency_Alert_System_Backend.emergency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyEventResponse {

    private Long eventId;
    private String status;
    private LocalDateTime createdAt;
    private RoomInfo room;
    private MemberInfo triggeredBy;
    private List<RecipientInfo> recipients;
    private int notificationSuccessCount;
    private int notificationFailureCount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomInfo {
        private Long id;
        private String roomNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberInfo {
        private Long memberId;
        private String memberName;
        private String email;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipientInfo {
        private Long memberId;
        private String memberName;
        private String email;
        private int deviceCount;
        private boolean notified;
        private String status;
    }
}

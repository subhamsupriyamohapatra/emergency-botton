package com.example.Emergency_Alert_System_Backend.room.service;

import com.example.Emergency_Alert_System_Backend.room.entity.Room;
import com.example.Emergency_Alert_System_Backend.room.repository.RoomRepository;
//import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    @Transactional
    public Room createRoom(Room room) {

        if (roomRepository.existsByRoomNumber(room.getRoomNumber())) {
            throw new IllegalArgumentException(
                    "Room number already exists"
            );
        }
        return roomRepository.save(room);
    }

    @Transactional(readOnly = true)
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Room getRoomById(Long id){
        return roomRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Room not found with id "+id));
    }


}

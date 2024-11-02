package com.naji.gjensidige.sHotel.controller;

import java.sql.Blob;
import java.util.List;
import java.util.ArrayList;
import java.util.Base64;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import com.naji.gjensidige.sHotel.model.Room;
import org.springframework.http.ResponseEntity;
import com.naji.gjensidige.sHotel.service.IRoomService;
import org.springframework.web.multipart.MultipartFile;
import com.naji.gjensidige.sHotel.response.RoomResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final IRoomService roomService;

    // Add new Room
    @PostMapping("/add/new-room")
    public ResponseEntity<RoomResponse> addNewRoom(@RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType") String roomType, @RequestParam("roomPrice") BigDecimal roomPrice)
            throws IOException, SQLException {

        if (photo == null || roomType == null || roomPrice == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Room savedRomm = roomService.addNewRoom(photo, roomType, roomPrice);

        RoomResponse response = new RoomResponse(savedRomm.getId(), savedRomm.getRoomType(), savedRomm.getRoomPrice());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/room/types")
    public List<String> findRoomTypes() {
        return roomService.findDistinctRoomTypes();
    }

    // Get All Rooms
    @GetMapping
    public ResponseEntity<Page<RoomResponse>> getAllRooms(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "filter", defaultValue = "") String filter) {

        Page<Room> rooms = roomService.findFilteredRooms(filter, page, size);

        Page<RoomResponse> roomResponses = rooms.map(room -> {
            RoomResponse response = new RoomResponse();

            response.setRoomType(room.getRoomType());
            response.setRoomPrice(room.getRoomPrice());
            response.setId(room.getId());

            try {
                Blob photBlob = room.getPhoto();

                if (photBlob != null) {
                    byte[] photoByte = photBlob.getBytes(1, (int) photBlob.length());
                    String base64 = Base64.getEncoder().encodeToString(photoByte);
                    response.setPhoto(base64);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error converting photo to base64: " + e.getMessage());
            }
            return response;
        });
        return ResponseEntity.ok().body(roomResponses);
    }

    // get Room By Id
    @GetMapping("/room/{id}")
    public RoomResponse getRoomById(@PathVariable long id) {
        Room room = roomService.getRoomById(id);
        RoomResponse roomResponse = new RoomResponse();

        roomResponse.setId(room.getId());
        ;
        roomResponse.setBooked(room.isBooked());
        roomResponse.setRoomPrice(room.getRoomPrice());
        roomResponse.setRoomType(room.getRoomType());

        try {
            Blob photoBlob = room.getPhoto();
            if (photoBlob != null) {
                byte[] photoByte = photoBlob.getBytes(1, (int) photoBlob.length());
                String base64 = Base64.getEncoder().encodeToString(photoByte);
                roomResponse.setPhoto(base64);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error converting photo to base64: " + e.getMessage());

        }

        return roomResponse;
    }

    // Find all Availabel rooms
    @GetMapping("/available-rooms")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(
            @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam("roomType") String roomType) throws SQLException {

        List<Room> availableRooms = roomService.getAvailableRooms(checkInDate, checkOutDate, roomType);
        List<RoomResponse> roomResponses = new ArrayList<>();

        for (Room room : availableRooms) {

            RoomResponse roomResponse = new RoomResponse();
            roomResponse.setId(room.getId());
            roomResponse.setRoomPrice(room.getRoomPrice());
            roomResponse.setRoomType(room.getRoomType());

            Blob blobPhoto = room.getPhoto();

            if (blobPhoto != null) {
                byte[] photByte = blobPhoto.getBytes(1, (int) blobPhoto.length());
                String base64Photo = Base64.getEncoder().encodeToString(photByte);

                roomResponse.setPhoto(base64Photo);

                roomResponses.add(roomResponse);
            }
        }
        if (roomResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(roomResponses);
    };

    // Delete Room By Id
    @DeleteMapping("/delete/room/{id}")
    public void deleteRoomById(@PathVariable long id) {
        roomService.deleteRoom(id);
    }

}

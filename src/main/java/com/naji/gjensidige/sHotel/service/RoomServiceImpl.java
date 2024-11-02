package com.naji.gjensidige.sHotel.service;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import javax.sql.rowset.serial.SerialBlob;
import org.springframework.data.domain.Page;
import com.naji.gjensidige.sHotel.model.Room;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;
import com.naji.gjensidige.sHotel.exception.ResourceNotFoundException;
import com.naji.gjensidige.sHotel.repository.RoomRepository;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService {

    private final RoomRepository roomRepository;

    @Override
    public Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice) {
        // Create a new Room object using the no-argument constructor
        Room room = new Room();

        // Set the room type and room price properties on the Room object
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);

        // Check if the provided photo is not empty
        if (!photo.isEmpty()) {
            try {
                // Convert the uploaded photo (MultipartFile) into a byte array
                byte[] photoByte = photo.getBytes();

                // Convert the byte array into a Blob object
                // SerialBlob is a serializable implementation of the Blob interface
                Blob photBlob = new SerialBlob(photoByte);

                // Set the Blob object as the photo for the Room object
                room.setPhoto(photBlob);

            } catch (Exception e) {
                // Handle any exceptions that occur during the conversion process
                System.out.println("Adding Room Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        // Save the Room object to the repository and return the saved object
        return roomRepository.save(room);
    }

    @Override
    public List<String> findDistinctRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Page<Room> findAllRooms(int page, int size) {
        return roomRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Page<Room> findFilteredRooms(String roomType, int page, int size) {
        return roomRepository.findByRoomTypeContainingIgnoreCase(roomType, PageRequest.of(page, size));
    }

    @Override
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    @Override
    public Room updateRoom(Long id, MultipartFile photo, String roomType, BigDecimal roomPrice) {

        return null;
    }

    @Override
    public Room getRoomById(long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room Not found with id: " + id));
    }

    @Override
    public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {

        return roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, roomType);

    }

}

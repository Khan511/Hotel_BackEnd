package com.naji.gjensidige.sHotel.service;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import com.naji.gjensidige.sHotel.model.Room;
import org.springframework.web.multipart.MultipartFile;

public interface IRoomService {

    Room addNewRoom(MultipartFile photo, String rooString, BigDecimal roomPrice);

    List<String> findDistinctRoomTypes();

    Page<Room> findAllRooms(int page, int size);

    Page<Room> findFilteredRooms(String filter, int page, int size);

    void deleteRoom(Long id);

    Room updateRoom(Long id, MultipartFile photo, String roomType, BigDecimal roomPrice);

    Room getRoomById(long id);

    List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
}

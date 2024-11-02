package com.naji.gjensidige.sHotel.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import com.naji.gjensidige.sHotel.model.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT DISTINCT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();

    @Query("SELECT r FROM Room r WHERE LOWER(r.roomType) LIKE LOWER(CONCAT('%', :roomType, '%'))")
    Page<Room> findByRoomTypeContainingIgnoreCase(@Param("roomType") String roomType, Pageable pageable);

    @Query("SELECT r FROM Room r " +
            "WHERE r.roomType LIKE %:roomType% " +
            "AND r.id NOT IN (" +
            "SELECT br.room.id FROM Booking br " +
            "WHERE ((br.checkInDate < :checkOutDate) AND (br.checkOutDate > :checkInDate))" +
            ")")
    List<Room> findAvailableRoomsByDatesAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

}
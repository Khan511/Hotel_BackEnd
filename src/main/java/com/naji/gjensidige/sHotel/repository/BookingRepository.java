package com.naji.gjensidige.sHotel.repository;

import java.util.List;
import java.util.Optional;

import com.naji.gjensidige.sHotel.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

   Optional<Booking> findByBookingConformationCode(String confirmationCode);

    List<Booking> findBookingsByRoomId(Long roomId);

}

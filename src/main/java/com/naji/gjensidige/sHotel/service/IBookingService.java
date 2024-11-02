package com.naji.gjensidige.sHotel.service;

import java.util.List;
import com.naji.gjensidige.sHotel.model.Booking;

public interface IBookingService {

    List<Booking> getAllBookings();

    String saveBooking(Long roomId, Booking bookingRequest);

    void cancelBooking(Long bookingId);

    Booking findByBookingConfirmationCode(String confirmationCode);

}

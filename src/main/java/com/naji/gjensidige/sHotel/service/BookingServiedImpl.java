package com.naji.gjensidige.sHotel.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import com.naji.gjensidige.sHotel.model.Room;
import org.springframework.stereotype.Service;
import com.naji.gjensidige.sHotel.model.Booking;
import com.naji.gjensidige.sHotel.repository.BookingRepository;
import com.naji.gjensidige.sHotel.exception.InvalidBookingRequestException;
import com.naji.gjensidige.sHotel.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class BookingServiedImpl implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IRoomService roomService;

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    public List<Booking> getAllBookingsByRoomId(Long roomId) {
        return bookingRepository.findBookingsByRoomId(roomId);
    }

    @Override
    public String saveBooking(Long roomId, Booking bookingRequest) {

        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
            throw new InvalidBookingRequestException("Check-In date must come before Check-Out date");
        }
        Room room = roomService.getRoomById(roomId);
        List<Booking> existingBookings = room.getBookings();

        boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBookings);
        if (roomIsAvailable) {
            room.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);

        } else {
            throw new InvalidBookingRequestException("Sorry, This room is not available for the selected dates.");
        }
        return bookingRequest.getBookingConformationCode();
    }

    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(
                        existingBooking -> bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate())
                                &&
                                bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckInDate()));

    }

    // Find by booking confirmation code
    @Override
    public Booking findByBookingConfirmationCode(String confirmationCode) {

        return bookingRepository.findByBookingConformationCode(confirmationCode).orElseThrow(
                () -> new ResourceNotFoundException("No Booking found with booking code: " + confirmationCode));

    }

}

package com.naji.gjensidige.sHotel.controller;

import java.util.List;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import com.naji.gjensidige.sHotel.model.Room;
import org.springframework.http.ResponseEntity;
import com.naji.gjensidige.sHotel.model.Booking;
import com.naji.gjensidige.sHotel.service.IRoomService;
import com.naji.gjensidige.sHotel.response.RoomResponse;
import com.naji.gjensidige.sHotel.service.IBookingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.naji.gjensidige.sHotel.response.BookingResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.naji.gjensidige.sHotel.exception.ResourceNotFoundException;
import com.naji.gjensidige.sHotel.exception.InvalidBookingRequestException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final IBookingService bookingService;
    private final IRoomService roomService;

    // Find All Bookings
    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<Booking> allBookings = bookingService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();

        for (Booking booking : allBookings) {
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    // Find Booking By Confirmation Code
    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConformationCode(@PathVariable String confirmationCode) {
        try {
            Booking booking = bookingService.findByBookingConfirmationCode(confirmationCode);
            if(booking == null){
                throw new ResourceNotFoundException("Booking not found with confirmaton code: " + confirmationCode );
            }
            BookingResponse bookingResponse = getBookingResponse(booking);

            return ResponseEntity.ok(bookingResponse);
        } catch (ResourceNotFoundException e) {
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        }
    }

    // Private function to get BookingResponse
    private BookingResponse getBookingResponse(Booking booking) {
        Room theRoom = roomService.getRoomById(booking.getRoom().getId());

        try {
            RoomResponse roomResponse = new RoomResponse(theRoom.getId(),
                    theRoom.getRoomType(),
                    theRoom.getRoomPrice());

            return new BookingResponse(booking.getId(), booking.getCheckInDate(),
                    booking.getCheckOutDate(),
                    booking.getGuestFullName(),
                    booking.getGuestEmail(),
                    booking.getNumberOfAdults(),
                    booking.getNumberOfCHildren(),
                    booking.getTotalNumberOfGuests(),
                    booking.getBookingConformationCode(),
                    roomResponse);
        } catch (ResourceNotFoundException e) {

            throw new ResourceNotFoundException("Room not found with ID: " +
                    booking.getId());
        }
    }

    // Save Booking
    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId, @RequestBody Booking bookingRequest) {

         try {
            String confirmationCode = bookingService.saveBooking(roomId, bookingRequest);
            return ResponseEntity
                    .ok("Room booked successfully, Your booking confirmation code is: " + confirmationCode);
        } catch (InvalidBookingRequestException e) {
            return ResponseEntity.badRequest().body("Sorry, This room is not available for the selected dates.");
        }
    };

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
    }

}

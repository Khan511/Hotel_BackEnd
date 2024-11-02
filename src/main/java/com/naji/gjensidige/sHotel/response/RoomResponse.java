package com.naji.gjensidige.sHotel.response;

import lombok.Data;
import java.util.List;
import java.math.BigDecimal;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Base64;

@Data
@NoArgsConstructor
public class RoomResponse {

    private long id;

    private String roomType;

    private BigDecimal roomPrice;

    private boolean isBooked;

    private String photo;

    private List<BookingResponse> bookings;

    public RoomResponse(long id, String roomType, BigDecimal roomPrice) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
    }

    public RoomResponse(long id, String roomType, BigDecimal roomPrice, boolean isBooked, byte[] photoByte,
            List<BookingResponse> bookings) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = isBooked;
        this.photo = photoByte != null ? Base64.encodeBase64String(photoByte) : null;
        this.bookings = bookings;
    }

}

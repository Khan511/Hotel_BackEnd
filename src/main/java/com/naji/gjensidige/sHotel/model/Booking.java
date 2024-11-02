package com.naji.gjensidige.sHotel.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "check_in")
    private LocalDate checkInDate;

    @Column(name = "check_iut")
    private LocalDate checkOutDate;

    @Column(name = "guest_full_name")
    private String guestFullName;

    @Column(name = "guest_email")
    private String guestEmail;

    @Column(name = "adults")
    private int numberOfAdults;

    @Column(name = "children")
    private int numberOfCHildren;

    @Column(name = "total_guest")
    private int totalNumberOfGuests;

    @Column(name = "confirmation_code")
    private String bookingConformationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
      @JsonBackReference
    private Room room;

    public void caculateTotalNumberOfGuests() {
        this.totalNumberOfGuests = this.numberOfAdults + this.numberOfCHildren;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;

        caculateTotalNumberOfGuests();
    }

    public void setNumberOfCHildren(int numberOfCHildren) {
        this.numberOfCHildren = numberOfCHildren;
        caculateTotalNumberOfGuests();
    }

    public void setBookingConformationCode(String bookingConformationCode) {
        this.bookingConformationCode = bookingConformationCode;
    }

}

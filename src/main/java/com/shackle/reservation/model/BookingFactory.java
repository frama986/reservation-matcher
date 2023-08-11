package com.shackle.reservation.model;

import com.exploreshackle.api.reservation.v1.Reservation;

public class BookingFactory {

    private BookingFactory(){}

    public static ReservationMatcher booking(Reservation reservation) {
        if(! reservation.getWebConfirmationCode().isBlank()) {
            return new OnlineBooking(reservation);
        }
        else if(! reservation.getBookingConfirmationNumber().isBlank()) {
            return new DirectBooking(reservation);
        }
        else {
            return new TravelAgentBooking(reservation);
        }
    }
}

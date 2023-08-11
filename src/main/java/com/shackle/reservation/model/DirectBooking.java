package com.shackle.reservation.model;

import com.exploreshackle.api.reservation.v1.Reservation;
import lombok.ToString;

@ToString
public class DirectBooking extends BookingEntry {

    DirectBooking(Reservation reservation) {
        super(reservation);
    }

    @Override
    public String getConfirmationCode() {
        return getReservation().getBookingConfirmationNumber();
    }
}

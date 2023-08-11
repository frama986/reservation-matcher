package com.shackle.reservation.model;

import com.exploreshackle.api.reservation.v1.Reservation;
import lombok.ToString;

@ToString
public class OnlineBooking extends BookingEntry {

    OnlineBooking(Reservation reservation) {
        super(reservation);
    }

    @Override
    public String getConfirmationCode() {
        return getReservation().getWebConfirmationCode();
    }
}

package com.shackle.reservation.model;

import com.exploreshackle.api.reservation.v1.Reservation;
import lombok.ToString;

@ToString
public class TravelAgentBooking extends BookingEntry {

    TravelAgentBooking(Reservation reservation) {
        super(reservation);
    }

    @Override
    public String getConfirmationCode() {
        return getReservation().getTravelAgentConfirmationCode();
    }
}

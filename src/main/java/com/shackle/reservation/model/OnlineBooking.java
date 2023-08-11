package com.shackle.reservation.model;

import com.exploreshackle.api.reservation.v1.Reservation;
import com.shackle.api.matcher.v1.SearchBookingRequest;
import lombok.ToString;

@ToString
public class OnlineBooking implements ReservationMatcher {

    private final Reservation reservation;

    public OnlineBooking(Reservation reservation) {
        this.reservation = reservation;
    }

    @Override
    public boolean match(SearchBookingRequest searchBookingRequest) {
        return getConfirmationCode().equals(searchBookingRequest.getConfirmationNumber());
    }

    @Override
    public Reservation getReservation() {
        return reservation;
    }

    @Override
    public String getConfirmationCode() {
        return reservation.getWebConfirmationCode();
    }

    @Override
    public long getInternalId() {
        return reservation.getInternalId();
    }
}

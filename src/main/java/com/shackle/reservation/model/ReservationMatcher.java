package com.shackle.reservation.model;

import com.exploreshackle.api.reservation.v1.Reservation;
import com.shackle.api.matcher.v1.SearchBookingRequest;
import lombok.ToString;

public interface ReservationMatcher {

    boolean match(SearchBookingRequest reservation);

    Reservation getReservation();

    String getConfirmationCode();

    long getInternalId();

    default boolean equals(ReservationMatcher other) {
        return this.getInternalId() == other.getInternalId();
    }
}

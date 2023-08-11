package com.shackle.reservation.model;

import com.exploreshackle.api.reservation.v1.GuestDetails;
import com.exploreshackle.api.reservation.v1.Reservation;
import com.shackle.api.matcher.v1.SearchBookingRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public abstract class BookingEntry {

    private final Reservation reservation;

    BookingEntry(Reservation reservation) {
        this.reservation = reservation;
    }

    public boolean match(SearchBookingRequest searchBookingRequest) {
        if (getConfirmationCode().equals(searchBookingRequest.getConfirmationNumber())) {
            return true;
        }
        if(compareGuestDetails(searchBookingRequest.getGuestDetails())) {
            return true;
        }
        return false;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public abstract String getConfirmationCode();

    public long getInternalId() {
        return reservation.getInternalId();
    }

    private boolean compareGuestDetails(GuestDetails other) {
        GuestDetails guestDetails = reservation.getGuestDetails();
        return false;
    }
}

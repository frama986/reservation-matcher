package com.shackle.reservation.model;

import com.exploreshackle.api.reservation.v1.GuestDetails;
import com.exploreshackle.api.reservation.v1.LocalDate;
import com.exploreshackle.api.reservation.v1.Reservation;
import com.shackle.api.matcher.v1.SearchBookingRequest;
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
        if (compareGuestDetails(searchBookingRequest.getGuestDetails())) {
            if (searchBookingRequest.hasArrivalDate() && searchBookingRequest.hasDepartureDate()) {
                return compareDates(searchBookingRequest.getArrivalDate(), searchBookingRequest.getDepartureDate());
            } else {
                return true;
            }
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

        return guestDetails.getEmail().equals(other.getEmail()) ||
                guestDetails.getPhoneNumber().equals(other.getPhoneNumber()) ||
                guestDetails.getLastName().equals(other.getLastName());
    }

    private boolean compareDates(LocalDate otherArrivalDate, LocalDate otherDepartureDate) {
        LocalDate arrivalDate = reservation.getArrivalDate();
        LocalDate departureDate = reservation.getDepartureDate();

        return arrivalDate.getDay() == otherArrivalDate.getDay()
                && arrivalDate.getMonth() == otherArrivalDate.getMonth()
                && arrivalDate.getYear() == otherArrivalDate.getYear()
                && departureDate.getDay() == otherDepartureDate.getDay()
                && departureDate.getMonth() == otherDepartureDate.getMonth()
                && departureDate.getYear() == otherDepartureDate.getYear();
    }
}

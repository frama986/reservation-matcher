package com.shackle.reservation.model;

import com.exploreshackle.api.reservation.v1.GuestDetails;
import com.shackle.api.matcher.v1.SearchBookingRequest;
import com.shackle.reservation.ReservationGeneratorUtil;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.shackle.reservation.ReservationGeneratorUtil.generateReservation;
import static org.junit.jupiter.api.Assertions.assertFalse;

@QuarkusTest
class ReservationCollectionTest {

    @Inject
    ReservationCollection reservationCollection;

    @Test
    public void whenANewReservationIsAddedThenItIsPersisted() {
        reservationCollection.add(generateReservation(ReservationGeneratorUtil.JOHN_WHITE_DATA));

        assertFalse(reservationCollection.getList().isEmpty());
    }

    @Test
    public void whenTheConfirmationNumberMatchesThenReturnAllTheMatches() {
        reservationCollection.add(generateReservation(ReservationGeneratorUtil.JOHN_WHITE_DATA));

        SearchBookingRequest request = SearchBookingRequest.newBuilder().setConfirmationNumber("AWT-ERT").build();
        List<BookingEntry> bookingEntries = reservationCollection.find(request);

        assertFalse(bookingEntries.isEmpty());
    }

    @Test
    public void whenTheEmailMatchesABookingThenReturnAllTheMatches() {
        reservationCollection.add(generateReservation(ReservationGeneratorUtil.JOHN_WHITE_DATA));
        reservationCollection.add(generateReservation(ReservationGeneratorUtil.MARTIN_RED_DATA));
        reservationCollection.add(generateReservation(ReservationGeneratorUtil.CARL_BLUE_DATA));

        SearchBookingRequest request = SearchBookingRequest.newBuilder().setGuestDetails(
                GuestDetails.newBuilder().setEmail("jsmith@gmail.com").build()
        ).build();
        List<BookingEntry> bookingEntries = reservationCollection.find(request);

        assertFalse(bookingEntries.isEmpty());
    }
}
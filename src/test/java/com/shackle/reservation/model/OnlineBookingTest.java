package com.shackle.reservation.model;

import com.exploreshackle.api.reservation.v1.GuestDetails;
import com.shackle.api.matcher.v1.SearchBookingRequest;
import com.shackle.reservation.ReservationGeneratorUtil;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class OnlineBookingTest {

    @Test
    public void whenConfirmationCodeMatchesThenReturnTrue() {
        BookingEntry bookingEntry = BookingFactory.newBookingEntry(ReservationGeneratorUtil.JOHN_WHITE_RESERVATION);

        SearchBookingRequest request = SearchBookingRequest.newBuilder().setConfirmationNumber("AWT-ERT").build();

        assertTrue(bookingEntry.match(request));
    }

    @Test
    public void whenEmailMatchesThenReturnTrue() {
        BookingEntry bookingEntry = BookingFactory.newBookingEntry(ReservationGeneratorUtil.JOHN_WHITE_RESERVATION);

        SearchBookingRequest request = SearchBookingRequest.newBuilder().setGuestDetails(
                GuestDetails.newBuilder().setEmail(ReservationGeneratorUtil.JOHN_WHITE_DATA.email()).build()
        ).build();

        assertTrue(bookingEntry.match(request));
    }
}
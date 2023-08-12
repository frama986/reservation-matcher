package com.shackle.reservation.model;

import com.shackle.reservation.ReservationGeneratorUtil;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class BookingFactoryTest {

    @Test
    public void whenWebConfirmationCodeIsPresentThenCreateOnlineBooking() {
        BookingEntry bookingEntry = BookingFactory.newBookingEntry(ReservationGeneratorUtil.JOHN_WHITE_RESERVATION);

        assertInstanceOf(OnlineBooking.class, bookingEntry);
    }
}
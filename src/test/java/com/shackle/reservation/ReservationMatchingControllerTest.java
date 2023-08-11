package com.shackle.reservation;

import com.exploreshackle.api.reservation.v1.Reservation;
import com.exploreshackle.api.reservation.v1.ReservationService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

@QuarkusTest
class ReservationMatchingControllerTest {

    @InjectMock
    ReservationService reservationServiceClient;

//    @Test
    public void testGetList() {
//        Mockito.when(reservationServiceClient.streamReservations(Mockito.any())).thenReturn(Multi.createFrom())
        Assertions.assertTrue(true);
    }

//    private Reservation generateReservation() {
//        return Reservation.newBuilder()
//                .setArrivalDate()
//                .setDepartureDate()
//                .setBookingConfirmationNumber()
//                .setGuestDetails()
//                .setInternalId()
//                .setTimestamp()
//                .setTravelAgentConfirmationCode()
//                .setWebConfirmationCode()
//                .build();
//    }
}
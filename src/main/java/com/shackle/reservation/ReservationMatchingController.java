package com.shackle.reservation;

import com.exploreshackle.api.reservation.v1.Reservation;
import com.shackle.reservation.model.ReservationMatcher;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("reservations")
public class ReservationMatchingController {

    @Inject
    ReservationLoaderService reservationLoaderService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReservationMatcher> getList() {
        return reservationLoaderService.getReservations();
    }
}

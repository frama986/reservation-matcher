package com.shackle.reservation;

import com.exploreshackle.api.reservation.v1.Reservation;
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
    public List<Reservation> getList() {
        return reservationLoaderService.getReservations();
    }
}

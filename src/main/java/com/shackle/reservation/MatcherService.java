package com.shackle.reservation;

import com.exploreshackle.api.reservation.v1.Reservation;
import com.shackle.api.matcher.v1.MatchedReservation;
import com.shackle.api.matcher.v1.SearchBookingRequest;
import com.shackle.api.matcher.v1.SearchBookingResponse;
import com.shackle.reservation.error.NotFoundException;
import com.shackle.reservation.model.BookingEntry;
import com.shackle.reservation.model.ReservationCollection;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;

import java.util.List;

@GrpcService
public class MatcherService implements com.shackle.api.matcher.v1.MatcherService {

    @Inject
    ReservationCollection reservationCollection;

    @Override
    public Uni<SearchBookingResponse> matchReservation(SearchBookingRequest request) {
        try {
            return Uni.createFrom().item(match(request));
        }
        catch (NotFoundException e) {
            return Uni.createFrom().failure(e);
        }
    }

    private SearchBookingResponse match(SearchBookingRequest searchBookingRequest) {
        List<BookingEntry> result = reservationCollection.find(searchBookingRequest);

        SearchBookingResponse response;
        if(result.isEmpty()) {
            // throw not found error
            throw new NotFoundException();
        } else if (result.size() == 1) {
            response = buildResponse(result.get(0));
        } else { // multiple matches
            // we need more information to identify the exact reservation
            if (searchBookingRequest.getArrivalDate() == null || searchBookingRequest.getDepartureDate() == null) {
                response = buildResponse("Insufficient information - Please provide the arrival date");
            }
            else {
                response = buildResponse("Insufficient information");
            }
        }
        return response;
    }

    private SearchBookingResponse buildResponse(BookingEntry bookingEntry) {
        Reservation reservation = bookingEntry.getReservation();
        return SearchBookingResponse.newBuilder().setReservation(
                        MatchedReservation.newBuilder()
                                .setConfirmationNumber(bookingEntry.getConfirmationCode())
                                .setArrivalDate(reservation.getArrivalDate())
                                .setDepartureDate(reservation.getDepartureDate())
                                .setGuestDetails(reservation.getGuestDetails())
                                .setTimestamp(reservation.getTimestamp())
                                .build())
                .build();
    }

    private SearchBookingResponse buildResponse(String message) {
        return SearchBookingResponse.newBuilder().setMessage(message).build();
    }
}

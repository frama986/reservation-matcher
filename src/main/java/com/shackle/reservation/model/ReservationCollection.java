package com.shackle.reservation.model;

import com.exploreshackle.api.reservation.v1.Reservation;
import com.shackle.api.matcher.v1.SearchBookingRequest;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;

@ApplicationScoped
public class ReservationCollection {

    Map<String, List<ReservationMatcher>> store = new HashMap<>();

    public void add(Reservation reservation) {
        ReservationMatcher booking = BookingFactory.booking(reservation);

        // We assume that we cannot receive the same reservation twice
        store.computeIfAbsent(booking.getConfirmationCode(), k -> new LinkedList<>()).add(booking);
    }

    public List<ReservationMatcher> find(SearchBookingRequest searchBookingRequest) {
        return findByCode(searchBookingRequest).orElse(match(searchBookingRequest));

    }

    public List<ReservationMatcher> getList() {
        return store.values().stream().flatMap(l -> l.stream()).toList();
    }

    private Optional<List<ReservationMatcher>> findByCode(SearchBookingRequest searchBookingRequest) {
        return Optional.ofNullable(store.get(searchBookingRequest.getConfirmationNumber()));
    }

    private List<ReservationMatcher> match(SearchBookingRequest searchBookingRequest) {
        return store.values().stream().flatMap(l -> l.stream()).filter(r -> r.match(searchBookingRequest)).toList();
    }
}

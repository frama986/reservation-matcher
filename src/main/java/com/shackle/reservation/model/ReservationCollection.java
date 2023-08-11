package com.shackle.reservation.model;

import com.exploreshackle.api.reservation.v1.Reservation;
import com.shackle.api.matcher.v1.SearchBookingRequest;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;

@ApplicationScoped
public class ReservationCollection {

    Map<String, List<BookingEntry>> store = new HashMap<>();

    public void add(Reservation reservation) {
        BookingEntry bookingEntry = BookingFactory.newBookingEntry(reservation);

        // We assume that we cannot receive the same reservation twice
        store.computeIfAbsent(bookingEntry.getConfirmationCode(), k -> new LinkedList<>()).add(bookingEntry);
    }

    public List<BookingEntry> find(SearchBookingRequest searchBookingRequest) {
        return findByCode(searchBookingRequest).orElse(match(searchBookingRequest));

    }

    public List<BookingEntry> getList() {
        return store.values().stream().flatMap(l -> l.stream()).toList();
    }

    private Optional<List<BookingEntry>> findByCode(SearchBookingRequest searchBookingRequest) {
        return Optional.ofNullable(store.get(searchBookingRequest.getConfirmationNumber()));
    }

    private List<BookingEntry> match(SearchBookingRequest searchBookingRequest) {
        return store.values().stream().flatMap(l -> l.stream()).filter(r -> r.match(searchBookingRequest)).toList();
    }
}

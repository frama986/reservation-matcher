package com.shackle.reservation.error;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("Reservation not found");
    }
}

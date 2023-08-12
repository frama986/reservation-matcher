package com.shackle.reservation;

import com.exploreshackle.api.reservation.v1.GuestDetails;
import com.exploreshackle.api.reservation.v1.LocalDate;
import com.exploreshackle.api.reservation.v1.Reservation;

import java.util.List;

public class ReservationGeneratorUtil {

    public static List<Reservation> reservations(List<Data> data) {
        return data.stream().map(d -> generateReservation(d)).toList();
    }

    public static Reservation generateReservation(Data data) {
        return Reservation.newBuilder()
                .setWebConfirmationCode(data.confirmationCode())
                .setTimestamp(System.currentTimeMillis())
                .setInternalId(201379)
                .setGuestDetails(generateGuestDetails(data))
                .setArrivalDate(generateLocalDate(data.arrival()))
                .setDepartureDate(generateLocalDate(data.departure()))
                .build();
    }

    public static GuestDetails generateGuestDetails(Data data) {
        return GuestDetails.newBuilder()
                .setTitle("Mr")
                .setFirstName(data.name())
                .setLastName(data.surname())
                .setAddress("Random address")
                .setPostalCode("E3 3YB")
                .setCity("London")
                .setCountry("UK")
                .setCompanyName("my company")
                .setEmail(data.email())
                .setPhoneNumber(data.phoneNumber())
                .build();
    }

    public static LocalDate generateLocalDate(java.time.LocalDate date) {
        return LocalDate.newBuilder()
                .setDay(date.getDayOfMonth())
                .setMonth(date.getMonthValue())
                .setYear(date.getYear())
                .build();
    }

    public record Data(String confirmationCode, String name, String surname, String email,
                       String phoneNumber, java.time.LocalDate arrival, java.time.LocalDate departure) {
    }
}

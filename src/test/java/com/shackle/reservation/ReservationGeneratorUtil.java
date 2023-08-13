package com.shackle.reservation;

import com.exploreshackle.api.reservation.v1.GuestDetails;
import com.exploreshackle.api.reservation.v1.LocalDate;
import com.exploreshackle.api.reservation.v1.Reservation;
import com.shackle.reservation.model.BookingEntry;
import com.shackle.reservation.model.BookingFactory;

import java.util.List;

public class ReservationGeneratorUtil {

    public static final Data JOHN_WHITE_DATA = new ReservationGeneratorUtil.Data("AWT-ERT", "John", "White",
            "jsmith@gmail.com", "07756947311", java.time.LocalDate.of(2023, 11, 10),
            java.time.LocalDate.of(2023, 11, 23));
    public static final Data MARTIN_RED_DATA = new ReservationGeneratorUtil.Data("UTR-BGT", "Martin", "Red",
            "mred@gmail.com", "07756947322", java.time.LocalDate.of(2023, 11, 15),
            java.time.LocalDate.of(2023, 11, 23));
    public static final Data CARL_BLUE_DATA = new ReservationGeneratorUtil.Data("POY-VCX", "Carl", "Blue",
            "cblue@gmail.com", "07756947333", java.time.LocalDate.of(2023, 12, 10),
            java.time.LocalDate.of(2023, 12, 23));

    public static final Reservation JOHN_WHITE_RESERVATION = generateReservation(JOHN_WHITE_DATA);

    public static BookingEntry bookingEntry(Data data) {
        return BookingFactory.newBookingEntry(generateReservation(data));
    }

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

package com.shackle.reservation;

import com.shackle.api.matcher.v1.MatcherService;
import com.shackle.api.matcher.v1.SearchBookingRequest;
import com.shackle.api.matcher.v1.SearchBookingResponse;
import com.shackle.reservation.model.ReservationCollection;
import io.grpc.StatusRuntimeException;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.shackle.reservation.ReservationGeneratorUtil.bookingEntry;
import static com.shackle.reservation.ReservationGeneratorUtil.generateLocalDate;
import static io.grpc.Status.Code.NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class MatcherServiceTest {

    @GrpcClient
    MatcherService matcherServiceClient;

    @InjectMock
    ReservationCollection reservationCollection;

    @Test
    public void whenResultIsEmptyThenReturnsNotFoundMessage() throws ExecutionException, InterruptedException {
        Mockito.when(reservationCollection.find(Mockito.any(SearchBookingRequest.class))).thenReturn(
                Collections.emptyList());

        CompletableFuture<SearchBookingResponse> response = new CompletableFuture<>();
        CompletableFuture<Throwable> error = new CompletableFuture<>();

        matcherServiceClient.matchReservation(SearchBookingRequest.newBuilder().build()).subscribe().with(
                response::complete, error::complete);

        Throwable throwable = error.get();
        assertInstanceOf(StatusRuntimeException.class, throwable);
        assertEquals(NOT_FOUND, ((StatusRuntimeException) throwable).getStatus().getCode());
    }

    @Test
    public void whenMultipleResultsAndDatesAreNotProvidedThenAsksMoreInformation() throws ExecutionException, InterruptedException {
        Mockito.when(reservationCollection.find(Mockito.any(SearchBookingRequest.class)))
                .thenReturn(List.of(bookingEntry(ReservationGeneratorUtil.JOHN_WHITE_DATA),
                        bookingEntry(ReservationGeneratorUtil.MARTIN_RED_DATA)));

        CompletableFuture<SearchBookingResponse> response = new CompletableFuture<>();
        CompletableFuture<Throwable> error = new CompletableFuture<>();

        matcherServiceClient.matchReservation(SearchBookingRequest.newBuilder().build()).subscribe().with(
                response::complete, error::complete);

        SearchBookingResponse searchBookingResponse = response.get();
        assertEquals("Insufficient information - Please provide the arrival date", searchBookingResponse.getMessage());
        assertFalse(searchBookingResponse.hasReservation());
    }

    @Test
    public void whenMultipleResultsAndDatesAreProvidedThenReturnsInsufficientInformation() throws ExecutionException, InterruptedException {
        Mockito.when(reservationCollection.find(Mockito.any(SearchBookingRequest.class))).thenReturn(
                List.of(bookingEntry(ReservationGeneratorUtil.JOHN_WHITE_DATA),
                        bookingEntry(ReservationGeneratorUtil.MARTIN_RED_DATA)));

        CompletableFuture<SearchBookingResponse> response = new CompletableFuture<>();
        CompletableFuture<Throwable> error = new CompletableFuture<>();

        matcherServiceClient.matchReservation(generateBookingRequestWithDates()).subscribe().with(response::complete,
                error::complete);

        SearchBookingResponse searchBookingResponse = response.get();
        assertEquals("Insufficient information", searchBookingResponse.getMessage());
        assertFalse(searchBookingResponse.hasReservation());
    }

    @Test
    public void whenResultIsUniqueThenReturnTheReservation() throws ExecutionException, InterruptedException {
        Mockito.when(reservationCollection.find(Mockito.any(SearchBookingRequest.class))).thenReturn(
                Collections.singletonList(bookingEntry(ReservationGeneratorUtil.JOHN_WHITE_DATA)));

        CompletableFuture<SearchBookingResponse> response = new CompletableFuture<>();
        CompletableFuture<Throwable> error = new CompletableFuture<>();

        matcherServiceClient.matchReservation(SearchBookingRequest.newBuilder().build()).subscribe().with(
                response::complete, error::complete);

        assertEquals("AWT-ERT", response.get().getReservation().getConfirmationNumber());
    }

    private SearchBookingRequest generateBookingRequestWithDates() {
        return SearchBookingRequest.newBuilder()
                .setArrivalDate(generateLocalDate(LocalDate.of(2023, 01, 01)))
                .setDepartureDate(generateLocalDate(LocalDate.of(2023, 02, 01)))
                .build();
    }
}
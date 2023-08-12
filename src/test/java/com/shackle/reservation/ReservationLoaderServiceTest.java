package com.shackle.reservation;

import com.exploreshackle.api.reservation.v1.ReservationService;
import com.exploreshackle.api.reservation.v1.StreamReservationsRequest;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@QuarkusTest
class ReservationLoaderServiceTest {

    @Inject
    ReservationLoaderService reservationLoaderService;

    @InjectMock
    @GrpcClient("reservationServiceClient")
    ReservationService reservationServiceClient;

    @BeforeEach
    public void setup() {
        when(reservationServiceClient.streamReservations(Mockito.any(StreamReservationsRequest.class)))
                .thenReturn(Multi.createFrom().iterable(ReservationGeneratorUtil.reservations(testData())));
    }

    @Test
    public void whenStreamReturnsReservationsThenRecordsAreSaved() {
        // onStart is called before the stubbing of the method, so we need to call it again
        reservationLoaderService.onStart(new StartupEvent());

        assertEquals(3, reservationLoaderService.getReservations().size());
    }

    private List<ReservationGeneratorUtil.Data> testData() {
        return List.of(
                ReservationGeneratorUtil.JOHN_WHITE_DATA,
                ReservationGeneratorUtil.MARTIN_RED_DATA,
                ReservationGeneratorUtil.CARL_BLUE_DATA);
    }
}
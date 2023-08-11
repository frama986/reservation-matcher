package com.shackle.reservation;

import static org.junit.jupiter.api.Assertions.*;

import com.exploreshackle.api.reservation.v1.ReservationService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;

@QuarkusComponentTest
class ReservationLoaderServiceTest {

    @Inject
    ReservationLoaderService reservationLoaderService;

    @InjectMock
    ReservationService reservationServiceClient;

}
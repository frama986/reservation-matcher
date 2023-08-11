package com.shackle.reservation;

import com.exploreshackle.api.reservation.v1.Reservation;
import com.exploreshackle.api.reservation.v1.ReservationService;
import com.exploreshackle.api.reservation.v1.StreamReservationsRequest;
import com.shackle.reservation.model.BookingEntry;
import com.shackle.reservation.model.ReservationCollection;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.Cancellable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Startup
@ApplicationScoped
public class ReservationLoaderService {

  private static final Logger log = LoggerFactory.getLogger(ReservationLoaderService.class);

  @GrpcClient
  ReservationService reservationServiceClient;

  @Inject
  ReservationCollection reservationCollection;

  void onStart(@Observes StartupEvent ev) {
    log.info("Loading the reservations...");
    loadReservations();
  }

  void onStop(@Observes ShutdownEvent ev) {
    log.info("The application is stopping...");
  }

  private Cancellable loadReservations() {

    Multi<Reservation> reservationStream = reservationServiceClient
            .streamReservations(StreamReservationsRequest.newBuilder().build());

    return reservationStream.subscribe().with(
            item -> reservationCollection.add(item),
            failure -> log.info("Reservation stream error"),
            () -> log.info("Reservation stream completed")
    );
  }

  public List<BookingEntry> getReservations() {
    return reservationCollection.getList();
  }
}
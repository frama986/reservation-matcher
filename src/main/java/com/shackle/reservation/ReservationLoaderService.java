package com.shackle.reservation;

import com.exploreshackle.api.reservation.v1.Reservation;
import com.shackle.reservation.model.ReservationCollection;
import com.shackle.reservation.model.ReservationMatcher;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.Cancellable;
import jakarta.enterprise.context.ApplicationScoped;
import com.exploreshackle.api.reservation.v1.ReservationService;
import com.exploreshackle.api.reservation.v1.StreamReservationsRequest;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

@Startup
@ApplicationScoped
public class ReservationLoaderService {

  private static final Logger log = LoggerFactory.getLogger(ReservationLoaderService.class);

  @GrpcClient
  ReservationService reservationServiceClient;

  @Inject
  ReservationCollection reservationCollection;

  private List<ReservationMatcher> reservations;

  private Cancellable reservationSubscription;

  public void stopSubscription() {
    reservationSubscription.cancel();
  }

  void onStart(@Observes StartupEvent ev) {
    log.info("Loading the reservations...");
    reservationSubscription = loadReservations();
  }

  void onStop(@Observes ShutdownEvent ev) {
    log.info("The application is stopping...");
  }

  private Cancellable loadReservations() {

    reservations = new LinkedList<>();

    Multi<Reservation> reservationStream = reservationServiceClient
            .streamReservations(StreamReservationsRequest.newBuilder().build());

    return reservationStream.subscribe().with(
            item -> reservationCollection.add(item),
            failure -> log.info("Reservation stream error"),
            () -> log.info("Reservation stream completed")
    );
  }

  public List<ReservationMatcher> getReservations() {
    return reservationCollection.getList();
  }
}
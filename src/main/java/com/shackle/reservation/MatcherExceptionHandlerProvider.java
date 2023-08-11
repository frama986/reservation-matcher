package com.shackle.reservation;

import com.shackle.reservation.error.NotFoundException;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.quarkus.grpc.ExceptionHandler;
import io.quarkus.grpc.ExceptionHandlerProvider;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MatcherExceptionHandlerProvider implements ExceptionHandlerProvider {
    @Override
    public <ReqT, RespT> ExceptionHandler<ReqT, RespT> createHandler(ServerCall.Listener<ReqT> listener,
                                                                     ServerCall<ReqT, RespT> serverCall, Metadata metadata) {
        return new MatcherExceptionHandler<>(listener, serverCall, metadata);
    }

    @Override
    public Throwable transform(Throwable t) {
        if (t instanceof NotFoundException e) {
            return new StatusRuntimeException(Status.NOT_FOUND.withDescription(e.getMessage()));
        } else {
            return ExceptionHandlerProvider.toStatusException(t, true);
        }
    }

    private static class MatcherExceptionHandler<A, B> extends ExceptionHandler<A, B> {
        public MatcherExceptionHandler(ServerCall.Listener<A> listener, ServerCall<A, B> call, Metadata metadata) {
            super(listener, call, metadata);
        }

        @Override
        protected void handleException(Throwable t, ServerCall<A, B> call, Metadata metadata) {
            StatusRuntimeException sre = (StatusRuntimeException) ExceptionHandlerProvider.toStatusException(t, true);
            Metadata trailers = sre.getTrailers() != null ? sre.getTrailers() : metadata;
            call.close(sre.getStatus(), trailers);
        }
    }
}
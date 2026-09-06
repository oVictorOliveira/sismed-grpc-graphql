package dev.sismed.scheduling.service.exception;

import dev.sismed.scheduling.exception.ConsultaNotFoundException;
import dev.sismed.scheduling.exception.ConsultaStatusException;
import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GrpcExceptionAdvice {

    @GrpcExceptionHandler(ConsultaNotFoundException.class)
    public Status tratarNaoEncontrado(ConsultaNotFoundException e) {
        return Status.NOT_FOUND.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler(ConsultaStatusException.class)
    public Status tratarExcecaoStatus(ConsultaStatusException e) {
        return Status.FAILED_PRECONDITION.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler(IllegalArgumentException.class)
    public Status tratarArgumentoInvalido(IllegalArgumentException e) {
        return Status.INVALID_ARGUMENT.withDescription(e.getMessage());
    }
}

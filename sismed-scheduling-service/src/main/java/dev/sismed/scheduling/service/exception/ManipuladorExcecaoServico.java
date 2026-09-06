package dev.sismed.scheduling.service.exception;

import dev.sismed.scheduling.exception.ConsultaNaoEncontradaException;
import dev.sismed.scheduling.exception.ExcecaoStatusConsulta;
import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class ManipuladorExcecaoServico {

    @GrpcExceptionHandler(ConsultaNaoEncontradaException.class)
    public Status tratarNaoEncontrado(ConsultaNaoEncontradaException e) {
        return Status.NOT_FOUND.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler(ExcecaoStatusConsulta.class)
    public Status tratarExcecaoStatus(ExcecaoStatusConsulta e) {
        return Status.FAILED_PRECONDITION.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler(IllegalArgumentException.class)
    public Status tratarArgumentoInvalido(IllegalArgumentException e) {
        return Status.INVALID_ARGUMENT.withDescription(e.getMessage());
    }
}

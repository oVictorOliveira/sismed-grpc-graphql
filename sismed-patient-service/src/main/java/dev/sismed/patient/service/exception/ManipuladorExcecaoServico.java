package dev.sismed.patient.service.exception;

import dev.sismed.patient.exception.PacienteNaoEncontradoException;
import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class ManipuladorExcecaoServico {

    @GrpcExceptionHandler(PacienteNaoEncontradoException.class)
    public Status tratarPacienteNaoEncontrado(PacienteNaoEncontradoException e) {
        return Status.NOT_FOUND.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler(IllegalArgumentException.class)
    public Status tratarArgumentoInvalido(IllegalArgumentException e) {
        return Status.INVALID_ARGUMENT.withDescription(e.getMessage());
    }
}

package dev.sismed.scheduling.exception;

public class ConsultaNaoEncontradaException extends RuntimeException {

    public ConsultaNaoEncontradaException(Long id) {
        super("Consulta não encontrada: id=" + id);
    }
}

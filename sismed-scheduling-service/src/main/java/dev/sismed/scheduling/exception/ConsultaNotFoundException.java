package dev.sismed.scheduling.exception;

public class ConsultaNotFoundException extends RuntimeException {

    public ConsultaNotFoundException(Long id) {
        super("Consulta não encontrada: id=" + id);
    }
}

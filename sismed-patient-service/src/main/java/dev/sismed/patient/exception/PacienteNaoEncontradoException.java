package dev.sismed.patient.exception;

public class PacienteNaoEncontradoException extends RuntimeException {

    public PacienteNaoEncontradoException(Long id) {
        super("Paciente não encontrado: id=" + id);
    }
}

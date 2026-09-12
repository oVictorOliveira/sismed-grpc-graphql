package dev.sismed.scheduling.dto;

public record SchedulingDTO(
        Long id,
        Long pacienteId,
        Long medicoId,
        String dataHora,
        String especialidade,
        String observacoes,
        String status
) {
}

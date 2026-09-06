package dev.sismed.aggregator.controller;

import dev.sismed.aggregator.dto.*;
import dev.sismed.aggregator.security.AuthenticatedUsuario;
import dev.sismed.aggregator.security.entidade.UsuarioRole;
import dev.sismed.aggregator.service.AgendamentoClientService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AgendamentoController {

    private final AgendamentoClientService agendamentoClientService;

    public AgendamentoController(AgendamentoClientService agendamentoClientService) {
        this.agendamentoClientService = agendamentoClientService;
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    public ConsultaDTO buscarConsultaPorId(@Argument Long id) {
        return agendamentoClientService.buscarConsultaPorId(id);
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO', 'PACIENTE')")
    public List<ConsultaDTO> consultasPorPaciente(@Argument Long pacienteId,
                                                   @Argument Boolean apenasFuturas,
                                                   @AuthenticationPrincipal AuthenticatedUsuario usuario) {
        Long idFinal = usuario.getPapel() == UsuarioRole.PACIENTE ? usuario.getPacienteId() : pacienteId;
        return agendamentoClientService.consultasPorPaciente(idFinal, apenasFuturas);
    }

    @MutationMapping
    @PreAuthorize("hasRole('MEDICO')")
    public AgendarConsultaResponseDTO agendarConsulta(@Argument AgendarConsultaInputDTO entrada,
                                                       @AuthenticationPrincipal AuthenticatedUsuario usuario) {
        return agendamentoClientService.agendarConsulta(entrada, usuario.getUsername());
    }

    @MutationMapping
    @PreAuthorize("hasRole('MEDICO')")
    public MutacaoResponseDTO atualizarConsulta(@Argument Long id,
                                                 @Argument AtualizarConsultaInputDTO entrada) {
        return agendamentoClientService.atualizarConsulta(id, entrada);
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    public MutacaoResponseDTO confirmarConsulta(@Argument Long id) {
        return agendamentoClientService.confirmarConsulta(id);
    }

    @MutationMapping
    @PreAuthorize("hasRole('MEDICO')")
    public MutacaoResponseDTO realizarConsulta(@Argument Long id) {
        return agendamentoClientService.realizarConsulta(id);
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    public MutacaoResponseDTO cancelarConsulta(@Argument Long id) {
        return agendamentoClientService.cancelarConsulta(id);
    }
}

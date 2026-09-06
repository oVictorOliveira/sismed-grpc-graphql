package dev.sismed.aggregator.controller;

import dev.sismed.aggregator.dto.*;
import dev.sismed.aggregator.security.UsuarioAutenticado;
import dev.sismed.aggregator.security.entidade.PapelUsuario;
import dev.sismed.aggregator.service.ClienteServicoAgendamento;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ControladorAgendamento {

    private final ClienteServicoAgendamento clienteServicoAgendamento;

    public ControladorAgendamento(ClienteServicoAgendamento clienteServicoAgendamento) {
        this.clienteServicoAgendamento = clienteServicoAgendamento;
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    public ConsultaDTO buscarConsultaPorId(@Argument Long id) {
        return clienteServicoAgendamento.buscarConsultaPorId(id);
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO', 'PACIENTE')")
    public List<ConsultaDTO> consultasPorPaciente(@Argument Long pacienteId,
                                                   @Argument Boolean apenasFuturas,
                                                   @AuthenticationPrincipal UsuarioAutenticado usuario) {
        Long idFinal = usuario.getPapel() == PapelUsuario.PACIENTE ? usuario.getPacienteId() : pacienteId;
        return clienteServicoAgendamento.consultasPorPaciente(idFinal, apenasFuturas);
    }

    @MutationMapping
    @PreAuthorize("hasRole('MEDICO')")
    public RespostaAgendarConsultaDTO agendarConsulta(@Argument EntradaAgendarConsultaDTO entrada,
                                                       @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return clienteServicoAgendamento.agendarConsulta(entrada, usuario.getUsername());
    }

    @MutationMapping
    @PreAuthorize("hasRole('MEDICO')")
    public RespostaMutacaoDTO atualizarConsulta(@Argument Long id,
                                                 @Argument EntradaAtualizarConsultaDTO entrada) {
        return clienteServicoAgendamento.atualizarConsulta(id, entrada);
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    public RespostaMutacaoDTO confirmarConsulta(@Argument Long id) {
        return clienteServicoAgendamento.confirmarConsulta(id);
    }

    @MutationMapping
    @PreAuthorize("hasRole('MEDICO')")
    public RespostaMutacaoDTO realizarConsulta(@Argument Long id) {
        return clienteServicoAgendamento.realizarConsulta(id);
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    public RespostaMutacaoDTO cancelarConsulta(@Argument Long id) {
        return clienteServicoAgendamento.cancelarConsulta(id);
    }
}

package dev.sismed.aggregator.controller;

import dev.sismed.aggregator.dto.AdicionarPacienteResponseDTO;
import dev.sismed.aggregator.dto.PacienteDTO;
import dev.sismed.aggregator.dto.PacienteInputDTO;
import dev.sismed.aggregator.security.AuthenticatedUsuario;
import dev.sismed.aggregator.security.entidade.UsuarioRole;
import dev.sismed.aggregator.service.PacienteClientService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PacienteController {

    private final PacienteClientService pacienteClientService;

    public PacienteController(PacienteClientService pacienteClientService) {
        this.pacienteClientService = pacienteClientService;
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    public List<PacienteDTO> listarPacientes() {
        return pacienteClientService.listarPacientes();
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO', 'PACIENTE')")
    public PacienteDTO buscarPacientePorId(@Argument Long id,
                                            @AuthenticationPrincipal AuthenticatedUsuario usuario) {
        Long idConsulta = usuario.getPapel() == UsuarioRole.PACIENTE ? usuario.getPacienteId() : id;
        return pacienteClientService.buscarPacientePorId(idConsulta);
    }

    @MutationMapping
    @PreAuthorize("hasRole('MEDICO')")
    public AdicionarPacienteResponseDTO adicionarPaciente(@Argument PacienteInputDTO entrada) {
        return pacienteClientService.adicionarPaciente(entrada);
    }
}

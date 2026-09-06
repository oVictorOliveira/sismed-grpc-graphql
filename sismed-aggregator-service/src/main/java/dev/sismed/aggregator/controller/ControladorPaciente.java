package dev.sismed.aggregator.controller;

import dev.sismed.aggregator.dto.EntradaPacienteDTO;
import dev.sismed.aggregator.dto.PacienteDTO;
import dev.sismed.aggregator.dto.RespostaAdicionarPacienteDTO;
import dev.sismed.aggregator.security.UsuarioAutenticado;
import dev.sismed.aggregator.security.entidade.PapelUsuario;
import dev.sismed.aggregator.service.ClienteServicoPaciente;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ControladorPaciente {

    private final ClienteServicoPaciente clienteServicoPaciente;

    public ControladorPaciente(ClienteServicoPaciente clienteServicoPaciente) {
        this.clienteServicoPaciente = clienteServicoPaciente;
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    public List<PacienteDTO> listarPacientes() {
        return clienteServicoPaciente.listarPacientes();
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO', 'PACIENTE')")
    public PacienteDTO buscarPacientePorId(@Argument Long id,
                                            @AuthenticationPrincipal UsuarioAutenticado usuario) {
        Long idConsulta = usuario.getPapel() == PapelUsuario.PACIENTE ? usuario.getPacienteId() : id;
        return clienteServicoPaciente.buscarPacientePorId(idConsulta);
    }

    @MutationMapping
    @PreAuthorize("hasRole('MEDICO')")
    public RespostaAdicionarPacienteDTO adicionarPaciente(@Argument EntradaPacienteDTO entrada) {
        return clienteServicoPaciente.adicionarPaciente(entrada);
    }
}

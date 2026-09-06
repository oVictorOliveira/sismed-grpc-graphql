package dev.sismed.aggregator.service;

import dev.sismed.aggregator.dto.AdicionarPacienteResponseDTO;
import dev.sismed.aggregator.dto.PacienteDTO;
import dev.sismed.aggregator.dto.PacienteInputDTO;
import dev.sismed.patient.*;
import dev.sismed.patient.ServicoPacienteGrpc.ServicoPacienteBlockingStub;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteClientService {

    @GrpcClient("paciente")
    private ServicoPacienteBlockingStub stubPaciente;

    public List<PacienteDTO> listarPacientes() {
        var resposta = stubPaciente.listarPacientes(ListarPacientesRequisicao.newBuilder().build());
        return resposta.getPacientesList().stream().map(PacienteDTO::new).toList();
    }

    public PacienteDTO buscarPacientePorId(Long id) {
        var requisicao = BuscarPacientePorIdRequisicao.newBuilder().setId(id).build();
        var resposta = stubPaciente.buscarPacientePorId(requisicao);
        return new PacienteDTO(resposta.getPaciente());
    }

    public AdicionarPacienteResponseDTO adicionarPaciente(PacienteInputDTO entrada) {
        var proto = PacienteProto.newBuilder()
                .setNome(entrada.getNome())
                .setCpf(entrada.getCpf())
                .setEmail(entrada.getEmail())
                .setTelefone(entrada.getTelefone() != null ? entrada.getTelefone() : "")
                .setDataNascimento(entrada.getDataNascimento() != null ? entrada.getDataNascimento() : "")
                .setAtivo(true)
                .build();
        var requisicao = AdicionarPacienteRequisicao.newBuilder().setPaciente(proto).build();
        var resposta = stubPaciente.adicionarPaciente(requisicao);
        return new AdicionarPacienteResponseDTO(resposta.getPacienteId(), resposta.getMensagem());
    }
}

package dev.sismed.aggregator.service;

import dev.sismed.aggregator.dto.*;
import dev.sismed.scheduling.*;
import dev.sismed.scheduling.ServicoAgendamentoGrpc.ServicoAgendamentoBlockingStub;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendamentoClientService {

    @GrpcClient("agendamento")
    private ServicoAgendamentoBlockingStub stubAgendamento;

    public ConsultaDTO buscarConsultaPorId(Long id) {
        var resposta = stubAgendamento.buscarConsulta(BuscarConsultaRequisicao.newBuilder().setId(id).build());
        return new ConsultaDTO(resposta.getConsulta());
    }

    public List<ConsultaDTO> consultasPorPaciente(Long pacienteId, Boolean apenasFuturas) {
        var requisicao = ListarConsultasPorPacienteRequisicao.newBuilder()
                .setPacienteId(pacienteId)
                .setApenasFuturas(Boolean.TRUE.equals(apenasFuturas))
                .build();
        return stubAgendamento.listarConsultasPorPaciente(requisicao)
                .getConsultasList().stream().map(ConsultaDTO::new).toList();
    }

    public AgendarConsultaResponseDTO agendarConsulta(AgendarConsultaInputDTO entrada, String criadoPor) {
        var requisicao = AgendarConsultaRequisicao.newBuilder()
                .setPacienteId(entrada.getPacienteId())
                .setMedicoId(entrada.getMedicoId())
                .setDataHora(entrada.getDataHora())
                .setEspecialidade(entrada.getEspecialidade() != null ? entrada.getEspecialidade() : "")
                .setObservacoes(entrada.getObservacoes() != null ? entrada.getObservacoes() : "")
                .setCriadoPor(criadoPor != null ? criadoPor : "anonymous")
                .build();
        var resposta = stubAgendamento.agendarConsulta(requisicao);
        return new AgendarConsultaResponseDTO(resposta.getId(), resposta.getMensagem());
    }

    public MutacaoResponseDTO atualizarConsulta(Long id, AtualizarConsultaInputDTO entrada) {
        var requisicao = AtualizarConsultaRequisicao.newBuilder()
                .setId(id)
                .setDataHora(entrada.getDataHora() != null ? entrada.getDataHora() : "")
                .setEspecialidade(entrada.getEspecialidade() != null ? entrada.getEspecialidade() : "")
                .setObservacoes(entrada.getObservacoes() != null ? entrada.getObservacoes() : "")
                .build();
        var resposta = stubAgendamento.atualizarConsulta(requisicao);
        return new MutacaoResponseDTO(resposta.getMensagem());
    }

    public MutacaoResponseDTO confirmarConsulta(Long id) {
        var resposta = stubAgendamento.confirmarConsulta(
                ConfirmarConsultaRequisicao.newBuilder().setId(id).build());
        return new MutacaoResponseDTO(resposta.getMensagem());
    }

    public MutacaoResponseDTO realizarConsulta(Long id) {
        var resposta = stubAgendamento.realizarConsulta(
                RealizarConsultaRequisicao.newBuilder().setId(id).build());
        return new MutacaoResponseDTO(resposta.getMensagem());
    }

    public MutacaoResponseDTO cancelarConsulta(Long id) {
        var resposta = stubAgendamento.cancelarConsulta(
                CancelarConsultaRequisicao.newBuilder().setId(id).build());
        return new MutacaoResponseDTO(resposta.getMensagem());
    }
}

package dev.sismed.scheduling.service.handler;

import dev.sismed.scheduling.*;
import dev.sismed.scheduling.entity.Consulta;
import dev.sismed.scheduling.entity.ConsultaStatus;
import dev.sismed.scheduling.exception.ConsultaNaoEncontradaException;
import dev.sismed.scheduling.exception.ExcecaoStatusConsulta;
import dev.sismed.scheduling.repository.ConsultaRepositorio;
import dev.sismed.scheduling.util.MapadorEntidadeMensagem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ManipuladorRequisicaoAgendamento {

    private final ConsultaRepositorio repositorioConsulta;

    public ManipuladorRequisicaoAgendamento(ConsultaRepositorio repositorioConsulta) {
        this.repositorioConsulta = repositorioConsulta;
    }

    @Transactional
    public AgendarConsultaResposta agendarConsulta(AgendarConsultaRequisicao requisicao) {
        LocalDateTime dataHora = LocalDateTime.parse(requisicao.getDataHora(),
                MapadorEntidadeMensagem.FORMATADOR);
        if (!dataHora.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("A dataHora da consulta deve ser futura.");
        }
        Consulta consulta = MapadorEntidadeMensagem.paraEntidade(requisicao);
        repositorioConsulta.save(consulta);
        return AgendarConsultaResposta.newBuilder()
                .setId(consulta.getId())
                .setMensagem("Consulta agendada com sucesso")
                .build();
    }

    @Transactional(readOnly = true)
    public BuscarConsultaResposta buscarConsulta(BuscarConsultaRequisicao requisicao) {
        Consulta consulta = encontrarOuLancar(requisicao.getId());
        return MapadorEntidadeMensagem.paraRespostaBuscar(consulta);
    }

    @Transactional
    public AtualizarConsultaResposta atualizarConsulta(AtualizarConsultaRequisicao requisicao) {
        Consulta consulta = encontrarOuLancar(requisicao.getId());
        verificarEditavel(consulta);

        if (!requisicao.getDataHora().isBlank()) {
            LocalDateTime novaData = LocalDateTime.parse(requisicao.getDataHora(),
                    MapadorEntidadeMensagem.FORMATADOR);
            if (!novaData.isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException("A nova dataHora deve ser futura.");
            }
            consulta.setDataHora(novaData);
        }
        if (!requisicao.getEspecialidade().isBlank()) consulta.setEspecialidade(requisicao.getEspecialidade());
        if (!requisicao.getObservacoes().isBlank()) consulta.setObservacoes(requisicao.getObservacoes());

        repositorioConsulta.save(consulta);
        return AtualizarConsultaResposta.newBuilder().setMensagem("Consulta atualizada com sucesso").build();
    }

    @Transactional
    public CancelarConsultaResposta cancelarConsulta(CancelarConsultaRequisicao requisicao) {
        Consulta consulta = encontrarOuLancar(requisicao.getId());
        verificarEditavel(consulta);
        consulta.setStatus(ConsultaStatus.CANCELADA);
        repositorioConsulta.save(consulta);
        return CancelarConsultaResposta.newBuilder().setMensagem("Consulta cancelada").build();
    }

    @Transactional
    public ConfirmarConsultaResposta confirmarConsulta(ConfirmarConsultaRequisicao requisicao) {
        Consulta consulta = encontrarOuLancar(requisicao.getId());
        if (consulta.getStatus() != ConsultaStatus.AGENDADA) {
            throw new ExcecaoStatusConsulta("Apenas consultas AGENDADAS podem ser confirmadas. Status atual: "
                    + consulta.getStatus());
        }
        consulta.setStatus(ConsultaStatus.CONFIRMADA);
        repositorioConsulta.save(consulta);
        return ConfirmarConsultaResposta.newBuilder().setMensagem("Consulta confirmada").build();
    }

    @Transactional
    public RealizarConsultaResposta realizarConsulta(RealizarConsultaRequisicao requisicao) {
        Consulta consulta = encontrarOuLancar(requisicao.getId());
        if (consulta.getStatus() != ConsultaStatus.CONFIRMADA) {
            throw new ExcecaoStatusConsulta("Apenas consultas CONFIRMADAS podem ser realizadas. Status atual: "
                    + consulta.getStatus());
        }
        consulta.setStatus(ConsultaStatus.REALIZADA);
        repositorioConsulta.save(consulta);
        return RealizarConsultaResposta.newBuilder().setMensagem("Consulta realizada").build();
    }

    @Transactional(readOnly = true)
    public ListarConsultasPorPacienteResposta listarConsultasPorPaciente(ListarConsultasPorPacienteRequisicao requisicao) {
        List<Consulta> consultas = requisicao.getApenasFuturas()
                ? repositorioConsulta.buscarPorPacienteIdEDataHoraAposOrdenadoAsc(
                        requisicao.getPacienteId(), LocalDateTime.now())
                : repositorioConsulta.buscarPorPacienteIdOrdenadoPorDataHoraDesc(requisicao.getPacienteId());
        return MapadorEntidadeMensagem.paraRespostaListar(consultas);
    }

    @Transactional(readOnly = true)
    public ListarProximasConsultasResposta listarProximasConsultas(ListarProximasConsultasRequisicao requisicao) {
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.plusHours(requisicao.getDentroDeHoras());
        List<Consulta> consultas = repositorioConsulta.buscarPorDataHoraEntreEStatusEm(
                inicio, fim, List.of(ConsultaStatus.AGENDADA, ConsultaStatus.CONFIRMADA));
        return MapadorEntidadeMensagem.paraRespostaProximas(consultas);
    }

    private Consulta encontrarOuLancar(Long id) {
        return repositorioConsulta.findById(id)
                .orElseThrow(() -> new ConsultaNaoEncontradaException(id));
    }

    private void verificarEditavel(Consulta consulta) {
        if (consulta.getStatus() == ConsultaStatus.REALIZADA
                || consulta.getStatus() == ConsultaStatus.CANCELADA) {
            throw new ExcecaoStatusConsulta(
                    "Consulta com status " + consulta.getStatus() + " não pode ser alterada.");
        }
    }
}

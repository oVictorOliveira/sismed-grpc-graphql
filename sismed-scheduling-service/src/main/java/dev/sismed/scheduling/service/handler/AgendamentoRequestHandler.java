package dev.sismed.scheduling.service.handler;

import dev.sismed.scheduling.*;
import dev.sismed.scheduling.dto.SchedulingDTO;
import dev.sismed.scheduling.entity.Consulta;
import dev.sismed.scheduling.entity.ConsultaStatus;
import dev.sismed.scheduling.exception.ConsultaNotFoundException;
import dev.sismed.scheduling.exception.ConsultaStatusException;
import dev.sismed.scheduling.repository.ConsultaRepository;
import dev.sismed.scheduling.service.NotificationProducerService;
import dev.sismed.scheduling.util.AgendamentoMessageMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoRequestHandler {
    private final ConsultaRepository repositorioConsulta;
    private final NotificationProducerService notificationProducerService;

    public AgendamentoRequestHandler(ConsultaRepository repositorioConsulta, NotificationProducerService notificationProducerService) {
        this.repositorioConsulta = repositorioConsulta;
        this.notificationProducerService = notificationProducerService;
    }

    @Transactional
    public AgendarConsultaResposta agendarConsulta(AgendarConsultaRequisicao requisicao) {
        LocalDateTime dataHora = LocalDateTime.parse(requisicao.getDataHora(),
                AgendamentoMessageMapper.FORMATADOR);
        if (!dataHora.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("A dataHora da consulta deve ser futura.");
        }
        Consulta consulta = AgendamentoMessageMapper.paraEntidade(requisicao);

        Consulta consultaAgendada = repositorioConsulta.save(consulta);
        SchedulingDTO consultaDTO = new SchedulingDTO(consultaAgendada.getId(), consultaAgendada.getPacienteId(),
                consultaAgendada.getMedicoId(), consultaAgendada.getDataHora().toString(), consultaAgendada.getEspecialidade(),
                consultaAgendada.getObservacoes(), consultaAgendada.getStatus().toString());

        notificationProducerService.sendSchedulingNotification(consultaDTO);

        return AgendarConsultaResposta.newBuilder()
                .setId(consultaAgendada.getId())
                .setMensagem("Consulta agendada com sucesso")
                .build();
    }

    @Transactional(readOnly = true)
    public BuscarConsultaResposta buscarConsulta(BuscarConsultaRequisicao requisicao) {
        Consulta consulta = encontrarOuLancar(requisicao.getId());
        return AgendamentoMessageMapper.paraRespostaBuscar(consulta);
    }

    @Transactional
    public AtualizarConsultaResposta atualizarConsulta(AtualizarConsultaRequisicao requisicao) {
        Consulta consulta = encontrarOuLancar(requisicao.getId());
        verificarEditavel(consulta);

        if (!requisicao.getDataHora().isBlank()) {
            LocalDateTime novaData = LocalDateTime.parse(requisicao.getDataHora(),
                    AgendamentoMessageMapper.FORMATADOR);
            if (!novaData.isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException("A nova dataHora deve ser futura.");
            }
            consulta.setDataHora(novaData);
        }
        if (!requisicao.getEspecialidade().isBlank()) consulta.setEspecialidade(requisicao.getEspecialidade());
        if (!requisicao.getObservacoes().isBlank()) consulta.setObservacoes(requisicao.getObservacoes());

        Consulta consultaAlterada = repositorioConsulta.save(consulta);
        SchedulingDTO consultaDTO = new SchedulingDTO(consultaAlterada.getId(), consultaAlterada.getPacienteId(),
                consultaAlterada.getMedicoId(), consultaAlterada.getDataHora().toString(), consultaAlterada.getEspecialidade(),
                consultaAlterada.getObservacoes(), consultaAlterada.getStatus().toString());

        notificationProducerService.sendSchedulingNotification(consultaDTO);

        return AtualizarConsultaResposta.newBuilder().setMensagem("Consulta atualizada com sucesso").build();
    }

    @Transactional
    public CancelarConsultaResposta cancelarConsulta(CancelarConsultaRequisicao requisicao) {
        Consulta consulta = encontrarOuLancar(requisicao.getId());
        verificarEditavel(consulta);
        consulta.setStatus(ConsultaStatus.CANCELADA);

        Consulta consultaCancelada = repositorioConsulta.save(consulta);
        SchedulingDTO consultaDTO = new SchedulingDTO(consultaCancelada.getId(), consultaCancelada.getPacienteId(),
                consultaCancelada.getMedicoId(), consultaCancelada.getDataHora().toString(), consultaCancelada.getEspecialidade(),
                consultaCancelada.getObservacoes(), consultaCancelada.getStatus().toString());

        notificationProducerService.sendSchedulingNotification(consultaDTO);

        return CancelarConsultaResposta.newBuilder().setMensagem("Consulta cancelada").build();
    }

    @Transactional
    public ConfirmarConsultaResposta confirmarConsulta(ConfirmarConsultaRequisicao requisicao) {
        Consulta consulta = encontrarOuLancar(requisicao.getId());
        if (consulta.getStatus() != ConsultaStatus.AGENDADA) {
            throw new ConsultaStatusException("Apenas consultas AGENDADAS podem ser confirmadas. Status atual: "
                    + consulta.getStatus());
        }
        consulta.setStatus(ConsultaStatus.CONFIRMADA);

        Consulta consultaConfirmada = repositorioConsulta.save(consulta);
        SchedulingDTO consultaDTO = new SchedulingDTO(consultaConfirmada.getId(), consultaConfirmada.getPacienteId(),
                consultaConfirmada.getMedicoId(), consultaConfirmada.getDataHora().toString(), consultaConfirmada.getEspecialidade(),
                consultaConfirmada.getObservacoes(), consultaConfirmada.getStatus().toString());

        notificationProducerService.sendSchedulingNotification(consultaDTO);

        return ConfirmarConsultaResposta.newBuilder().setMensagem("Consulta confirmada").build();
    }

    @Transactional
    public RealizarConsultaResposta realizarConsulta(RealizarConsultaRequisicao requisicao) {
        Consulta consulta = encontrarOuLancar(requisicao.getId());
        if (consulta.getStatus() != ConsultaStatus.CONFIRMADA) {
            throw new ConsultaStatusException("Apenas consultas CONFIRMADAS podem ser realizadas. Status atual: "
                    + consulta.getStatus());
        }
        consulta.setStatus(ConsultaStatus.REALIZADA);

        Consulta consultaRealizada = repositorioConsulta.save(consulta);
        SchedulingDTO consultaDTO = new SchedulingDTO(consultaRealizada.getId(), consultaRealizada.getPacienteId(),
                consultaRealizada.getMedicoId(), consultaRealizada.getDataHora().toString(), consultaRealizada.getEspecialidade(),
                consultaRealizada.getObservacoes(), consultaRealizada.getStatus().toString());

        notificationProducerService.sendSchedulingNotification(consultaDTO);

        return RealizarConsultaResposta.newBuilder().setMensagem("Consulta realizada").build();
    }

    @Transactional(readOnly = true)
    public ListarConsultasPorPacienteResposta listarConsultasPorPaciente(ListarConsultasPorPacienteRequisicao requisicao) {
        List<Consulta> consultas = requisicao.getApenasFuturas()
                ? repositorioConsulta.buscarPorPacienteIdEDataHoraAposOrdenadoAsc(
                        requisicao.getPacienteId(), LocalDateTime.now())
                : repositorioConsulta.buscarPorPacienteIdOrdenadoPorDataHoraDesc(requisicao.getPacienteId());
        return AgendamentoMessageMapper.paraRespostaListar(consultas);
    }

    @Transactional(readOnly = true)
    public ListarProximasConsultasResposta listarProximasConsultas(ListarProximasConsultasRequisicao requisicao) {
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.plusHours(requisicao.getDentroDeHoras());
        List<Consulta> consultas = repositorioConsulta.buscarPorDataHoraEntreEStatusEm(
                inicio, fim, List.of(ConsultaStatus.AGENDADA, ConsultaStatus.CONFIRMADA));
        return AgendamentoMessageMapper.paraRespostaProximas(consultas);
    }

    private Consulta encontrarOuLancar(Long id) {
        return repositorioConsulta.findById(id)
                .orElseThrow(() -> new ConsultaNotFoundException(id));
    }

    private void verificarEditavel(Consulta consulta) {
        if (consulta.getStatus() == ConsultaStatus.REALIZADA
                || consulta.getStatus() == ConsultaStatus.CANCELADA) {
            throw new ConsultaStatusException(
                    "Consulta com status " + consulta.getStatus() + " não pode ser alterada.");
        }
    }
}

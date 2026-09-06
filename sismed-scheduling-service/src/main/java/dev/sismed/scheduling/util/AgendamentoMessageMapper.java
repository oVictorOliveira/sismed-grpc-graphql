package dev.sismed.scheduling.util;

import dev.sismed.scheduling.*;
import dev.sismed.scheduling.entity.Consulta;
import dev.sismed.scheduling.entity.ConsultaStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AgendamentoMessageMapper {

    public static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static Consulta paraEntidade(AgendarConsultaRequisicao req) {
        LocalDateTime dataHora = LocalDateTime.parse(req.getDataHora(), FORMATADOR);
        return new Consulta(req.getPacienteId(), req.getMedicoId(), dataHora,
                req.getEspecialidade(), req.getObservacoes(), req.getCriadoPor());
    }

    public static ConsultaProto paraProto(Consulta c) {
        return ConsultaProto.newBuilder()
                .setId(c.getId())
                .setPacienteId(c.getPacienteId())
                .setMedicoId(c.getMedicoId())
                .setDataHora(c.getDataHora().format(FORMATADOR))
                .setEspecialidade(c.getEspecialidade() != null ? c.getEspecialidade() : "")
                .setObservacoes(c.getObservacoes() != null ? c.getObservacoes() : "")
                .setStatus(ConsultaStatusProto.valueOf(c.getStatus().name()))
                .setCriadoPor(c.getCriadoPor() != null ? c.getCriadoPor() : "")
                .build();
    }

    public static BuscarConsultaResposta paraRespostaBuscar(Consulta c) {
        return BuscarConsultaResposta.newBuilder().setConsulta(paraProto(c)).build();
    }

    public static ListarConsultasPorPacienteResposta paraRespostaListar(List<Consulta> consultas) {
        return ListarConsultasPorPacienteResposta.newBuilder()
                .addAllConsultas(consultas.stream().map(AgendamentoMessageMapper::paraProto).toList())
                .build();
    }

    public static ListarProximasConsultasResposta paraRespostaProximas(List<Consulta> consultas) {
        return ListarProximasConsultasResposta.newBuilder()
                .addAllConsultas(consultas.stream().map(AgendamentoMessageMapper::paraProto).toList())
                .build();
    }

    public static ConsultaStatus deStatusProto(ConsultaStatusProto proto) {
        return ConsultaStatus.valueOf(proto.name());
    }
}

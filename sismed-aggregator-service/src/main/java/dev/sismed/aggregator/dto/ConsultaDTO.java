package dev.sismed.aggregator.dto;

import dev.sismed.scheduling.ConsultaProto;

public class ConsultaDTO {

    private Long id;
    private Long pacienteId;
    private Long medicoId;
    private String dataHora;
    private String especialidade;
    private String observacoes;
    private String status;
    private String criadoPor;

    public ConsultaDTO() {}

    public ConsultaDTO(ConsultaProto proto) {
        this.id = proto.getId();
        this.pacienteId = proto.getPacienteId();
        this.medicoId = proto.getMedicoId();
        this.dataHora = proto.getDataHora();
        this.especialidade = proto.getEspecialidade().isBlank() ? null : proto.getEspecialidade();
        this.observacoes = proto.getObservacoes().isBlank() ? null : proto.getObservacoes();
        this.status = proto.getStatus().name();
        this.criadoPor = proto.getCriadoPor().isBlank() ? null : proto.getCriadoPor();
    }

    public Long getId() { return id; }
    public Long getPacienteId() { return pacienteId; }
    public Long getMedicoId() { return medicoId; }
    public String getDataHora() { return dataHora; }
    public String getEspecialidade() { return especialidade; }
    public String getObservacoes() { return observacoes; }
    public String getStatus() { return status; }
    public String getCriadoPor() { return criadoPor; }
}

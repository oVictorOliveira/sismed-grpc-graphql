package dev.sismed.scheduling.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consulta", indexes = {
        @Index(name = "idx_consulta_paciente_data", columnList = "paciente_id, data_hora")
})
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    @Column(name = "medico_id", nullable = false)
    private Long medicoId;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    private String especialidade;

    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultaStatus status;

    @Column(name = "criado_por")
    private String criadoPor;

    public Consulta() {}

    public Consulta(Long pacienteId, Long medicoId, LocalDateTime dataHora,
                    String especialidade, String observacoes, String criadoPor) {
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.dataHora = dataHora;
        this.especialidade = especialidade;
        this.observacoes = observacoes;
        this.criadoPor = criadoPor;
        this.status = ConsultaStatus.AGENDADA;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public ConsultaStatus getStatus() { return status; }
    public void setStatus(ConsultaStatus status) { this.status = status; }

    public String getCriadoPor() { return criadoPor; }
    public void setCriadoPor(String criadoPor) { this.criadoPor = criadoPor; }
}

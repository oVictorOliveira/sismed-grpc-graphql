package dev.sismed.aggregator.dto;

import dev.sismed.patient.PacienteProto;

public class PacienteDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String dataNascimento;
    private boolean ativo;

    public PacienteDTO() {}

    public PacienteDTO(PacienteProto proto) {
        this.id = proto.getId();
        this.nome = proto.getNome();
        this.cpf = proto.getCpf();
        this.email = proto.getEmail();
        this.telefone = proto.getTelefone().isBlank() ? null : proto.getTelefone();
        this.dataNascimento = proto.getDataNascimento().isBlank() ? null : proto.getDataNascimento();
        this.ativo = proto.getAtivo();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getDataNascimento() { return dataNascimento; }
    public boolean isAtivo() { return ativo; }
}

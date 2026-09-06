package dev.sismed.aggregator.security.entidade;

import jakarta.persistence.*;

@Entity
@Table(name = "sismed_usuario")
public class SismedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UsuarioRole papel;

    @Column(name = "paciente_id")
    private Long pacienteId;

    public SismedUser() {}

    public SismedUser(String login, String senha, UsuarioRole papel, Long pacienteId) {
        this.login = login;
        this.senha = senha;
        this.papel = papel;
        this.pacienteId = pacienteId;
    }

    public Long getId() { return id; }
    public String getLogin() { return login; }
    public String getSenha() { return senha; }
    public UsuarioRole getPapel() { return papel; }
    public Long getPacienteId() { return pacienteId; }
}

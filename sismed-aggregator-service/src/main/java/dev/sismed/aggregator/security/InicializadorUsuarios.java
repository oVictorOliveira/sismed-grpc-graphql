package dev.sismed.aggregator.security;

import dev.sismed.aggregator.security.entidade.PapelUsuario;
import dev.sismed.aggregator.security.entidade.SismedUsuario;
import dev.sismed.aggregator.security.repositorio.UsuarioRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InicializadorUsuarios {

    @Bean
    public CommandLineRunner inicializar(UsuarioRepositorio repositorio, PasswordEncoder codificador) {
        return args -> {
            if (repositorio.count() > 0) return;

            repositorio.save(new SismedUsuario("dr.carlos",  codificador.encode("medico123"),     PapelUsuario.MEDICO,      null));
            repositorio.save(new SismedUsuario("enf.ana",    codificador.encode("enf123"),         PapelUsuario.ENFERMEIRO,  null));
            repositorio.save(new SismedUsuario("pac.joao",   codificador.encode("paciente123"),    PapelUsuario.PACIENTE,    1L));
            repositorio.save(new SismedUsuario("pac.maria",  codificador.encode("paciente123"),    PapelUsuario.PACIENTE,    2L));
        };
    }
}

package dev.sismed.aggregator.security;

import dev.sismed.aggregator.security.entidade.SismedUser;
import dev.sismed.aggregator.security.entidade.UsuarioRole;
import dev.sismed.aggregator.security.repositorio.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsuarioDataInitializer {

    @Bean
    public CommandLineRunner inicializar(UsuarioRepository repositorio, PasswordEncoder codificador) {
        return args -> {
            if (repositorio.count() > 0) return;

            repositorio.save(new SismedUser("dr.carlos",  codificador.encode("medico123"),   UsuarioRole.MEDICO,     null));
            repositorio.save(new SismedUser("enf.ana",    codificador.encode("enf123"),       UsuarioRole.ENFERMEIRO, null));
            repositorio.save(new SismedUser("pac.joao",   codificador.encode("paciente123"),  UsuarioRole.PACIENTE,   1L));
            repositorio.save(new SismedUser("pac.maria",  codificador.encode("paciente123"),  UsuarioRole.PACIENTE,   2L));
        };
    }
}

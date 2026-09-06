package dev.sismed.aggregator.security.repositorio;

import dev.sismed.aggregator.security.entidade.SismedUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<SismedUsuario, Long> {
    Optional<SismedUsuario> findByLogin(String login);
}

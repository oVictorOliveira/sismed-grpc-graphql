package dev.sismed.aggregator.security.repositorio;

import dev.sismed.aggregator.security.entidade.SismedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<SismedUser, Long> {
    Optional<SismedUser> findByLogin(String login);
}

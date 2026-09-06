package dev.sismed.patient.repository;

import dev.sismed.patient.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepositorio extends JpaRepository<Paciente, Long> {

    @Query("SELECT p FROM Paciente p WHERE p.cpf = :cpf")
    Optional<Paciente> buscarPorCpf(@Param("cpf") String cpf);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Paciente p WHERE p.cpf = :cpf")
    boolean existePorCpf(@Param("cpf") String cpf);
}

package dev.sismed.scheduling.repository;

import dev.sismed.scheduling.entity.Consulta;
import dev.sismed.scheduling.entity.ConsultaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    @Query("SELECT c FROM Consulta c WHERE c.pacienteId = :pacienteId ORDER BY c.dataHora DESC")
    List<Consulta> buscarPorPacienteIdOrdenadoPorDataHoraDesc(@Param("pacienteId") Long pacienteId);

    @Query("SELECT c FROM Consulta c WHERE c.pacienteId = :pacienteId AND c.dataHora > :dataHora ORDER BY c.dataHora ASC")
    List<Consulta> buscarPorPacienteIdEDataHoraAposOrdenadoAsc(@Param("pacienteId") Long pacienteId,
                                                                 @Param("dataHora") LocalDateTime dataHora);

    @Query("SELECT c FROM Consulta c WHERE c.dataHora BETWEEN :inicio AND :fim AND c.status IN :statuses")
    List<Consulta> buscarPorDataHoraEntreEStatusEm(@Param("inicio") LocalDateTime inicio,
                                                    @Param("fim") LocalDateTime fim,
                                                    @Param("statuses") List<ConsultaStatus> statuses);
}

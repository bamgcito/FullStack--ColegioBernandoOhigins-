package com.pablo.ms_horarios.repository;

import com.pablo.ms_horarios.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {

    List<Horario> findByCursoId(Long cursoId);

    List<Horario> findByProfesorId(Long profesorId);

    List<Horario> findByAsignacionDocenteId(Long asignacionDocenteId);

    List<Horario> findByDiaSemanaAndBloque(String diaSemana, Integer bloque);
}

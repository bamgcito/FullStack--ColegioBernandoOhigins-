package com.pablo.ms_asignatura.repository;

import com.pablo.ms_asignatura.model.AsignacionDocente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AsignacionDocenteRepository extends JpaRepository<AsignacionDocente, Long> {

    List<AsignacionDocente> findByCursoId(Long cursoId);

    List<AsignacionDocente> findByProfesorId(Long profesorId);

    boolean existsByAsignatura_IdAndProfesorIdAndCursoId(Long asignaturaId, Long profesorId, Long cursoId);
}

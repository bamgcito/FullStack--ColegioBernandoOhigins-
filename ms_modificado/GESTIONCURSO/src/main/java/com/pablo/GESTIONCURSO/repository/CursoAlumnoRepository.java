package com.pablo.GESTIONCURSO.repository;

import com.pablo.GESTIONCURSO.model.CursoAlumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoAlumnoRepository extends JpaRepository<CursoAlumno, Long> {

    boolean existsByCursoIdAndAlumnoId(Long cursoId, Long alumnoId);
    List<CursoAlumno> findByCursoId(Long cursoId);
}

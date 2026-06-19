package com.pablo.ms_cursos.repository;

import com.pablo.ms_cursos.model.Curso;
import com.pablo.ms_cursos.model.CursoAlumno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CursoAlumnoRepository extends JpaRepository<CursoAlumno, Long> {
    List<CursoAlumno> findByCurso(Curso curso);
    boolean existsByCursoAndAlumnoId(Curso curso, Long alumnoId);
    Optional<CursoAlumno> findByAlumnoId(Long alumnoId);
    long countByCurso(Curso curso);
}

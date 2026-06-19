package com.pablo.ms_cursos.repository;

import com.pablo.ms_cursos.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByProfesorJefeId(Long profesorJefeId);
}

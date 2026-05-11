package com.pablo.GESTIONCURSO.repository;

import com.pablo.GESTIONCURSO.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    boolean existsByNivelAndLetraAndAnio(String nivel, String letra, Integer anio);

    boolean existsByProfesorJefeId(Long profesorJefeId);
}

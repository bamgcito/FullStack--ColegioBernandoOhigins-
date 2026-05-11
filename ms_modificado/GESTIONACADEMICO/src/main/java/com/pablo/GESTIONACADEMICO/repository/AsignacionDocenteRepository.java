package com.pablo.GESTIONACADEMICO.repository;

import com.pablo.GESTIONACADEMICO.model.AsignacionDocente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AsignacionDocenteRepository extends JpaRepository<AsignacionDocente, Long> {

    List<AsignacionDocente> findByCursoId(Long cursoId);

    boolean existsByCursoIdAndAsignaturaId(Long cursoId, Long asignaturaId);

    Optional<AsignacionDocente> findByCursoIdAndAsignaturaId(Long cursoId, Long asignaturaId);
}

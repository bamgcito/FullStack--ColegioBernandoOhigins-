package com.pablo.GESTIONACADEMICO.repository;

import com.pablo.GESTIONACADEMICO.model.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {

    List<Evaluacion> findByAsignacionDocenteId(Long asignacionDocenteId);
}

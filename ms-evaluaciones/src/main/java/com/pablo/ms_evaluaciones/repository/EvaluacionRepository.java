package com.pablo.ms_evaluaciones.repository;

import com.pablo.ms_evaluaciones.model.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {

    List<Evaluacion> findByAsignacionId(Long asignacionId);
}

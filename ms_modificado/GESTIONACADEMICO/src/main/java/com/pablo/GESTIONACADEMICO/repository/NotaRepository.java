package com.pablo.GESTIONACADEMICO.repository;

import com.pablo.GESTIONACADEMICO.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotaRepository extends JpaRepository<Nota, Long> {

    List<Nota> findByEvaluacionId(Long evaluacionId);

    List<Nota> findByAlumnoId(Long alumnoId);

    boolean existsByAlumnoIdAndEvaluacionId(Long alumnoId, Long evaluacionId);
}

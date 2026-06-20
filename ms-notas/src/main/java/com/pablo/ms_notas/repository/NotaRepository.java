package com.pablo.ms_notas.repository;

import com.pablo.ms_notas.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NotaRepository extends JpaRepository<Nota, Long> {

    List<Nota> findByAlumnoId(Long alumnoId);

    List<Nota> findByEvaluacionId(Long evaluacionId);

    List<Nota> findByAlumnoIdAndEvaluacionId(Long alumnoId, Long evaluacionId);

    Optional<Nota> findFirstByAlumnoIdAndEvaluacionId(Long alumnoId, Long evaluacionId);
}

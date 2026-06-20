package com.pablo.ms_asistencia.repository;

import com.pablo.ms_asistencia.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByAlumnoId(Long alumnoId);

    List<Asistencia> findByAsignacionId(Long asignacionId);

    Optional<Asistencia> findByAlumnoIdAndAsignacionIdAndFecha(Long alumnoId, Long asignacionId, LocalDate fecha);
}

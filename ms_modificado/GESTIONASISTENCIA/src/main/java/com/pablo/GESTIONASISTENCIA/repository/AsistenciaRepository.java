package com.pablo.GESTIONASISTENCIA.repository;

import com.pablo.GESTIONASISTENCIA.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByAlumnoId(Long alumnoId);

    List<Asistencia> findByAsignacionDocenteId(Long asignacionDocenteId);

    boolean existsByAlumnoIdAndAsignacionDocenteIdAndFecha(Long alumnoId, Long asignacionDocenteId, String fecha);

    long countByAlumnoIdAndEstado(Long alumnoId, String estado);
}

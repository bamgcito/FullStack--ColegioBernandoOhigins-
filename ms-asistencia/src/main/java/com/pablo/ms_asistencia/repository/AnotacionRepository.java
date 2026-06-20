package com.pablo.ms_asistencia.repository;

import com.pablo.ms_asistencia.model.Anotacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnotacionRepository extends JpaRepository<Anotacion, Long> {

    List<Anotacion> findByAlumnoId(Long alumnoId);

    List<Anotacion> findByAsignacionId(Long asignacionId);
}

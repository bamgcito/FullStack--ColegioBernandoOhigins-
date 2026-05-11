package com.pablo.GESTIONASISTENCIA.repository;

import com.pablo.GESTIONASISTENCIA.model.Anotacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnotacionRepository extends JpaRepository<Anotacion, Long> {

    List<Anotacion> findByAlumnoId(Long alumnoId);

    List<Anotacion> findByAsignacionDocenteId(Long asignacionDocenteId);
}

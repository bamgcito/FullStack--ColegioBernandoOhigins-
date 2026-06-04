package com.pablo.microservicio.repository;

import com.pablo.microservicio.model.AlumnoApoderado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlumnoApoderadoRepository extends JpaRepository<AlumnoApoderado, Long> {
    boolean existsByAlumnoIdAndApoderadoId(Long alumnoId, Long apoderadoId);
    List<AlumnoApoderado> findByApoderadoId(Long apoderadoId);
    List<AlumnoApoderado> findByAlumnoId(Long alumnoId);
}

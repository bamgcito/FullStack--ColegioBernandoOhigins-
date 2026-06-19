package com.pablo.ms_perfiles.repository;

import com.pablo.ms_perfiles.model.Alumno;
import com.pablo.ms_perfiles.model.AlumnoApoderado;
import com.pablo.ms_perfiles.model.Apoderado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlumnoApoderadoRepository extends JpaRepository<AlumnoApoderado, Long> {
    List<AlumnoApoderado> findByApoderado(Apoderado apoderado);
    List<AlumnoApoderado> findByAlumno(Alumno alumno);
    boolean existsByAlumnoAndApoderado(Alumno alumno, Apoderado apoderado);
}

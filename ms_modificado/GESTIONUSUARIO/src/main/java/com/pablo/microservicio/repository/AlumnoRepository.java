package com.pablo.microservicio.repository;

import com.pablo.microservicio.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    Optional<Alumno> findByUsuarioId(Long usuarioId);
}

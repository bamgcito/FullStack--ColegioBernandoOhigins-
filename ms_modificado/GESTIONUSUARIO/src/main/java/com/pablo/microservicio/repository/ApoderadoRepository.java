package com.pablo.microservicio.repository;

import com.pablo.microservicio.model.Apoderado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApoderadoRepository extends JpaRepository<Apoderado, Long> {
    Optional<Apoderado> findByUsuarioId(Long usuarioId);
}

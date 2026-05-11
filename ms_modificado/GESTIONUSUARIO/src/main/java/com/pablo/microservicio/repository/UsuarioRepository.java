package com.pablo.microservicio.repository;

import com.pablo.microservicio.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByRut(String rut);
    boolean existsByRut(String rut);
}

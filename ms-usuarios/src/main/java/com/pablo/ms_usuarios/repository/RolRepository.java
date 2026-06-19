package com.pablo.ms_usuarios.repository;

import com.pablo.ms_usuarios.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombre(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}

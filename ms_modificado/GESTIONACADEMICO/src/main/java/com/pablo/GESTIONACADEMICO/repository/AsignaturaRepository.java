package com.pablo.GESTIONACADEMICO.repository;

import com.pablo.GESTIONACADEMICO.model.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {

    boolean existsByNombreIgnoreCase(String nombre);
}

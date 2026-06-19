package com.pablo.ms_perfiles.repository;

import com.pablo.ms_perfiles.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    Optional<Alumno> findByUsuarioId(Long usuarioId);
    Optional<Alumno> findByRut(String rut);
    boolean existsByRut(String rut);
    boolean existsByUsuarioId(Long usuarioId);
    @Transactional
    void deleteByUsuarioId(Long usuarioId);
}

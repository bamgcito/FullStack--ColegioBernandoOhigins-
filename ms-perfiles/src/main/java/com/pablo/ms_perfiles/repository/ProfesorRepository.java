package com.pablo.ms_perfiles.repository;

import com.pablo.ms_perfiles.model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
    Optional<Profesor> findByUsuarioId(Long usuarioId);
    Optional<Profesor> findByRut(String rut);
    boolean existsByRut(String rut);
    boolean existsByUsuarioId(Long usuarioId);
    @Transactional
    void deleteByUsuarioId(Long usuarioId);
}

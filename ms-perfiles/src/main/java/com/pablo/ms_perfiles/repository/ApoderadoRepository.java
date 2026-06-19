package com.pablo.ms_perfiles.repository;

import com.pablo.ms_perfiles.model.Apoderado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface ApoderadoRepository extends JpaRepository<Apoderado, Long> {
    Optional<Apoderado> findByUsuarioId(Long usuarioId);
    Optional<Apoderado> findByRut(String rut);
    boolean existsByRut(String rut);
    boolean existsByUsuarioId(Long usuarioId);
    @Transactional
    void deleteByUsuarioId(Long usuarioId);
}

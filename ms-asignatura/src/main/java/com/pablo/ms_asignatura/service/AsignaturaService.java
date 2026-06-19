package com.pablo.ms_asignatura.service;

import com.pablo.ms_asignatura.dto.AsignaturaDTO;
import com.pablo.ms_asignatura.model.Asignatura;
import com.pablo.ms_asignatura.repository.AsignaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AsignaturaService {

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    public ResponseEntity<Object> crearAsignatura(AsignaturaDTO solicitud) {
        Asignatura asignatura = new Asignatura();
        asignatura.setNombre(solicitud.getNombre());
        asignatura.setDescripcion(solicitud.getDescripcion());
        asignaturaRepository.save(asignatura);
        return ResponseEntity.ok(Map.of("mensaje", "Asignatura creada exitosamente: " + solicitud.getNombre()));
    }

    public List<AsignaturaDTO> listarTodas() {
        List<Asignatura> asignaturas = asignaturaRepository.findAll();
        List<AsignaturaDTO> resultado = new ArrayList<>();
        for (Asignatura a : asignaturas) {
            resultado.add(AsignaturaDTO.desde(a));
        }
        return resultado;
    }

    public ResponseEntity<Object> buscarPorId(Long id) {
        Asignatura a = asignaturaRepository.findById(id).orElse(null);
        if (a == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe asignatura con ID: " + id));
        }
        return ResponseEntity.ok(AsignaturaDTO.desde(a));
    }
}

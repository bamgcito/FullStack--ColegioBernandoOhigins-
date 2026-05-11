package com.pablo.microservicio.service;

import com.pablo.microservicio.dto.RolDTO;
import com.pablo.microservicio.model.Rol;
import com.pablo.microservicio.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public String crearRol(RolDTO solicitud) {
        if (rolRepository.existsByNombreIgnoreCase(solicitud.getNombre())) {
            return "El rol '" + solicitud.getNombre().toUpperCase() + "' ya existe.";
        }

        Rol rol = new Rol();
        rol.setNombre(solicitud.getNombre().toUpperCase());
        rolRepository.save(rol);
        return "Rol '" + rol.getNombre() + "' creado exitosamente.";
    }

    public List<RolDTO> listarRoles() {
        List<Rol> roles = rolRepository.findAll();
        List<RolDTO> lista = new ArrayList<>();
        for (Rol r : roles) {
            lista.add(RolDTO.desde(r));
        }
        return lista;
    }
}

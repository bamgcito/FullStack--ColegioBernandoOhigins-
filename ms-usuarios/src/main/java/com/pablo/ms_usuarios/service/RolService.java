package com.pablo.ms_usuarios.service;

import com.pablo.ms_usuarios.dto.RolDTO;
import com.pablo.ms_usuarios.model.Rol;
import com.pablo.ms_usuarios.repository.RolRepository;
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
            return "Ya existe un rol con el nombre: " + solicitud.getNombre();
        }
        Rol rol = new Rol();
        rol.setNombre(solicitud.getNombre().toUpperCase());
        rolRepository.save(rol);
        return "Rol creado exitosamente: " + solicitud.getNombre().toUpperCase();
    }

    public List<RolDTO> listarRoles() {
        List<Rol> roles = rolRepository.findAll();
        List<RolDTO> resultado = new ArrayList<>();
        for (Rol rol : roles) {
            resultado.add(RolDTO.desde(rol));
        }
        return resultado;
    }
}

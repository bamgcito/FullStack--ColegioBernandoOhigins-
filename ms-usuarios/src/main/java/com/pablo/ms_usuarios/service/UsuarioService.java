package com.pablo.ms_usuarios.service;

import com.pablo.ms_usuarios.dto.UsuarioDTO;
import com.pablo.ms_usuarios.model.Rol;
import com.pablo.ms_usuarios.model.Usuario;
import com.pablo.ms_usuarios.repository.RolRepository;
import com.pablo.ms_usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String crearUsuario(UsuarioDTO solicitud) {
        if (usuarioRepository.existsByRut(solicitud.getRut())) {
            return "Ya existe un usuario con el RUT: " + solicitud.getRut();
        }
        Rol rol = rolRepository.findById(solicitud.getRolId()).orElse(null);
        if (rol == null) {
            return "Rol no encontrado con ID: " + solicitud.getRolId();
        }
        Usuario usuario = new Usuario();
        usuario.setRut(solicitud.getRut());
        usuario.setContrasena(passwordEncoder.encode(solicitud.getContrasena()));
        usuario.setRol(rol);
        usuarioRepository.save(usuario);
        return "Usuario creado exitosamente con RUT: " + solicitud.getRut() + " y rol: " + rol.getNombre();
    }

    public UsuarioDTO buscarPorId(Long id) {
        Usuario u = usuarioRepository.findById(id).orElse(null);
        if (u == null) return null;
        return UsuarioDTO.desde(u);
    }

    public UsuarioDTO buscarPorRut(String rut) {
        Usuario u = usuarioRepository.findByRut(rut).orElse(null);
        if (u == null) return null;
        return UsuarioDTO.desde(u);
    }

    public ResponseEntity<String> eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe usuario con ID: " + id);
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok("Usuario eliminado correctamente.");
    }

    public List<UsuarioDTO> listarTodos() {
        List<Usuario> todos = usuarioRepository.findAll();
        List<UsuarioDTO> resultado = new ArrayList<>();
        for (Usuario u : todos) {
            resultado.add(UsuarioDTO.desde(u));
        }
        return resultado;
    }
}

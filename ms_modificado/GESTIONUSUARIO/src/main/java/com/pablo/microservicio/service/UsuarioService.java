package com.pablo.microservicio.service;

import com.pablo.microservicio.dto.*;
import com.pablo.microservicio.model.*;
import com.pablo.microservicio.repository.*;
import com.pablo.microservicio.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;
    @Autowired private AlumnoRepository alumnoRepository;
    @Autowired private ProfesorRepository profesorRepository;
    @Autowired private ApoderadoRepository apoderadoRepository;
    @Autowired private AlumnoApoderadoRepository alumnoApoderadoRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    public Object login(LoginDTO solicitud) {
        Usuario encontrado = usuarioRepository.findByRut(solicitud.getRut()).orElse(null);
        if (encontrado == null) {
            return "Usuario no encontrado con RUT: " + solicitud.getRut();
        }
        if (!passwordEncoder.matches(solicitud.getContrasena(), encontrado.getContrasena())) {
            return "Contraseña incorrecta.";
        }

        String token = jwtUtil.generarToken(encontrado.getRut(), encontrado.getRol().getNombre(), encontrado.getId());

        // Buscar nombre y apellido según el rol del usuario
        String nombre = "";
        String apellido = "";
        String rol = encontrado.getRol().getNombre();

        if (rol.equals("ALUMNO")) {
            Alumno a = alumnoRepository.findByUsuarioId(encontrado.getId()).orElse(null);
            if (a != null) { nombre = a.getNombre(); apellido = a.getApellido(); }
        } else if (rol.equals("PROFESOR")) {
            Profesor p = profesorRepository.findByUsuarioId(encontrado.getId()).orElse(null);
            if (p != null) { nombre = p.getNombre(); apellido = p.getApellido(); }
        } else if (rol.equals("APODERADO")) {
            Apoderado ap = apoderadoRepository.findByUsuarioId(encontrado.getId()).orElse(null);
            if (ap != null) { nombre = ap.getNombre(); apellido = ap.getApellido(); }
        } else if (rol.equals("ADMIN")) {
            nombre = "Administrador";
            apellido = "";
        }

        return LoginDTO.desde(encontrado, token, nombre, apellido);
    }

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

    public List<UsuarioDTO> listarTodos() {
        List<Usuario> todos = usuarioRepository.findAll();
        List<UsuarioDTO> lista = new ArrayList<>();
        for (Usuario u : todos) {
            lista.add(UsuarioDTO.desde(u));
        }
        return lista;
    }

    public String crearAlumno(AlumnoDTO solicitud) {
        Usuario usuario = usuarioRepository.findByRut(solicitud.getRut()).orElse(null);
        if (usuario == null) {
            return "No existe un usuario con RUT: " + solicitud.getRut();
        }
        Alumno alumno = Alumno.desde(solicitud, usuario);
        alumnoRepository.save(alumno);
        return "Perfil de alumno creado exitosamente para: " + solicitud.getNombre() + " " + solicitud.getApellido();
    }

    public String crearProfesor(ProfesorDTO solicitud) {
        Usuario usuario = usuarioRepository.findByRut(solicitud.getRut()).orElse(null);
        if (usuario == null) {
            return "No existe un usuario con RUT: " + solicitud.getRut();
        }
        Profesor profesor = Profesor.desde(solicitud, usuario);
        profesorRepository.save(profesor);
        return "Perfil de profesor creado exitosamente para: " + solicitud.getNombre() + " " + solicitud.getApellido();
    }

    public String crearApoderado(ApoderadoDTO solicitud) {
        Usuario usuario = usuarioRepository.findByRut(solicitud.getRut()).orElse(null);
        if (usuario == null) {
            return "No existe un usuario con RUT: " + solicitud.getRut();
        }
        Apoderado apoderado = Apoderado.desde(solicitud, usuario);
        apoderadoRepository.save(apoderado);
        return "Perfil de apoderado creado exitosamente para: " + solicitud.getNombre() + " " + solicitud.getApellido();
    }

    public String asociarApoderadoAAlumno(AsociarApoderadoDTO solicitud) {
        Usuario usuarioAlumno = usuarioRepository.findByRut(solicitud.getRutAlumno()).orElse(null);
        if (usuarioAlumno == null) {
            return "No existe un usuario con RUT: " + solicitud.getRutAlumno();
        }
        Alumno alumno = alumnoRepository.findByUsuarioId(usuarioAlumno.getId()).orElse(null);
        if (alumno == null) {
            return "El usuario con RUT " + solicitud.getRutAlumno() + " no tiene perfil de alumno.";
        }
        Usuario usuarioApoderado = usuarioRepository.findByRut(solicitud.getRutApoderado()).orElse(null);
        if (usuarioApoderado == null) {
            return "No existe un usuario con RUT: " + solicitud.getRutApoderado();
        }
        Apoderado apoderado = apoderadoRepository.findByUsuarioId(usuarioApoderado.getId()).orElse(null);
        if (apoderado == null) {
            return "El usuario con RUT " + solicitud.getRutApoderado() + " no tiene perfil de apoderado.";
        }
        if (alumnoApoderadoRepository.existsByAlumnoIdAndApoderadoId(alumno.getId(), apoderado.getId())) {
            return "El apoderado " + apoderado.getNombre() + " ya está asociado a este alumno.";
        }
        AlumnoApoderado asociacion = new AlumnoApoderado();
        asociacion.setAlumno(alumno);
        asociacion.setApoderado(apoderado);
        asociacion.setEsPrincipal(solicitud.getEsPrincipal() != null && solicitud.getEsPrincipal());
        alumnoApoderadoRepository.save(asociacion);
        return "Apoderado " + apoderado.getNombre() + " asociado exitosamente al alumno " + alumno.getNombre();
    }

    public Object obtenerInfoAlumno(String rutAlumno) {
        Usuario usuarioAlumno = usuarioRepository.findByRut(rutAlumno).orElse(null);
        if (usuarioAlumno == null) {
            return "No existe un usuario con RUT: " + rutAlumno;
        }
        Alumno alumno = alumnoRepository.findByUsuarioId(usuarioAlumno.getId()).orElse(null);
        if (alumno == null) {
            return "El usuario con RUT " + rutAlumno + " no tiene perfil de alumno.";
        }
        return AlumnoDTO.desde(alumno);
    }

    public AlumnoDTO buscarAlumnoPorId(Long usuarioId) {
        Alumno alumno = alumnoRepository.findByUsuarioId(usuarioId).orElse(null);
        if (alumno == null) return null;
        return AlumnoDTO.desde(alumno);
    }

    public Object buscarProfesorPorId(Long profesorUsuarioId) {
        Usuario usuario = usuarioRepository.findById(profesorUsuarioId).orElse(null);
        if (usuario == null) {
            return "Usuario no encontrado con ID: " + profesorUsuarioId;
        }
        Profesor profesor = profesorRepository.findByUsuarioId(profesorUsuarioId).orElse(null);
        if (profesor == null) {
            return "El usuario con ID " + profesorUsuarioId + " no tiene perfil de profesor.";
        }
        return ProfesorDTO.desde(profesor, usuario);
    }

    public boolean apoderadoTieneAccesoAAlumno(Long apoderadoUsuarioId, String rutAlumno) {
        Usuario usuarioAlumno = usuarioRepository.findByRut(rutAlumno).orElse(null);
        if (usuarioAlumno == null) return false;
        Alumno alumno = alumnoRepository.findByUsuarioId(usuarioAlumno.getId()).orElse(null);
        if (alumno == null) return false;
        Apoderado apoderado = apoderadoRepository.findByUsuarioId(apoderadoUsuarioId).orElse(null);
        if (apoderado == null) return false;
        return alumnoApoderadoRepository.existsByAlumnoIdAndApoderadoId(alumno.getId(), apoderado.getId());
    }

    public String bootstrap() {
        // Crear roles si no existen
        Rol adminRol = rolRepository.findById(1L).orElse(null);
        if (adminRol == null) {
            adminRol = new Rol();
            adminRol.setNombre("ADMIN");
            rolRepository.save(adminRol);
        }

        Rol profesorRol = rolRepository.findById(2L).orElse(null);
        if (profesorRol == null) {
            profesorRol = new Rol();
            profesorRol.setNombre("PROFESOR");
            rolRepository.save(profesorRol);
        }

        Rol alumnoRol = rolRepository.findById(3L).orElse(null);
        if (alumnoRol == null) {
            alumnoRol = new Rol();
            alumnoRol.setNombre("ALUMNO");
            rolRepository.save(alumnoRol);
        }

        Rol apoderadoRol = rolRepository.findById(4L).orElse(null);
        if (apoderadoRol == null) {
            apoderadoRol = new Rol();
            apoderadoRol.setNombre("APODERADO");
            rolRepository.save(apoderadoRol);
        }

        // Crear usuarios de prueba
        if (!usuarioRepository.existsByRut("12345678-9")) {
            Usuario admin = new Usuario();
            admin.setRut("12345678-9");
            admin.setContrasena(passwordEncoder.encode("admin123"));
            admin.setRol(adminRol);
            usuarioRepository.save(admin);
        }

        if (!usuarioRepository.existsByRut("15234567-8")) {
            Usuario profesor = new Usuario();
            profesor.setRut("15234567-8");
            profesor.setContrasena(passwordEncoder.encode("profe123"));
            profesor.setRol(profesorRol);
            usuarioRepository.save(profesor);
        }

        if (!usuarioRepository.existsByRut("20111222-3")) {
            Usuario alumno = new Usuario();
            alumno.setRut("20111222-3");
            alumno.setContrasena(passwordEncoder.encode("alumno123"));
            alumno.setRol(alumnoRol);
            usuarioRepository.save(alumno);
        }

        if (!usuarioRepository.existsByRut("30111222-K")) {
            Usuario apoderado = new Usuario();
            apoderado.setRut("30111222-K");
            apoderado.setContrasena(passwordEncoder.encode("apod123"));
            apoderado.setRol(apoderadoRol);
            usuarioRepository.save(apoderado);
        }

        return "Bootstrap completado. Usuarios de prueba creados.";
    }
}

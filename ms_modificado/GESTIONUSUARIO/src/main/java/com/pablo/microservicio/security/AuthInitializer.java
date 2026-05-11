package com.pablo.microservicio.security;

import com.pablo.microservicio.model.Rol;
import com.pablo.microservicio.model.Usuario;
import com.pablo.microservicio.repository.RolRepository;
import com.pablo.microservicio.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Crear roles si no existen
        if (rolRepository.count() == 0) {
            rolRepository.save(crearRol("ADMIN"));
            rolRepository.save(crearRol("PROFESOR"));
            rolRepository.save(crearRol("ALUMNO"));
            rolRepository.save(crearRol("APODERADO"));
            System.out.println("Roles creados correctamente.");
        }

        // Crear admin si no existe
        if (!usuarioRepository.existsByRut("12345678-9")) {
            Rol rolAdmin = rolRepository.findByNombre("ADMIN").orElse(null);

            Usuario admin = new Usuario();
            admin.setRut("12345678-9");
            admin.setContrasena(passwordEncoder.encode("admin123"));
            admin.setRol(rolAdmin);

            usuarioRepository.save(admin);
            System.out.println("Admin creado: RUT 12345678-9 / contraseña: admin123");
        }
    }

    private Rol crearRol(String nombre) {
        Rol rol = new Rol();
        rol.setNombre(nombre);
        return rol;
    }
}
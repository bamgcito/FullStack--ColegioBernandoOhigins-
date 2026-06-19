package com.pablo.ms_usuarios.controller;

import com.pablo.ms_usuarios.dto.RolDTO;
import com.pablo.ms_usuarios.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @PostMapping
    public String crearRol(@RequestBody RolDTO solicitud) {
        return rolService.crearRol(solicitud);
    }

    @GetMapping
    public List<RolDTO> listarRoles() {
        return rolService.listarRoles();
    }
}

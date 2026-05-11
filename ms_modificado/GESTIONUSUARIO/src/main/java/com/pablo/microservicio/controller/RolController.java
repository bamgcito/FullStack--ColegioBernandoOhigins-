package com.pablo.microservicio.controller;

import com.pablo.microservicio.dto.RolDTO;
import com.pablo.microservicio.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

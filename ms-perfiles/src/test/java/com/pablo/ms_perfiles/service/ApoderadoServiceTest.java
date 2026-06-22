package com.pablo.ms_perfiles.service;

import com.pablo.ms_perfiles.dto.ApoderadoDTO;
import com.pablo.ms_perfiles.dto.AsociarDTO;
import com.pablo.ms_perfiles.dto.UsuarioDTO;
import com.pablo.ms_perfiles.model.Alumno;
import com.pablo.ms_perfiles.model.AlumnoApoderado;
import com.pablo.ms_perfiles.model.Apoderado;
import com.pablo.ms_perfiles.repository.AlumnoApoderadoRepository;
import com.pablo.ms_perfiles.repository.AlumnoRepository;
import com.pablo.ms_perfiles.repository.ApoderadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApoderadoServiceTest {

    @Mock
    private ApoderadoRepository apoderadoRepository;

    @Mock
    private AlumnoRepository alumnoRepository;

    @Mock
    private AlumnoApoderadoRepository alumnoApoderadoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ApoderadoService apoderadoService;

    @Test
    void crearApoderado_usuarioNoExiste_retornaNotFound() {
        ApoderadoDTO dto = new ApoderadoDTO();
        dto.setRut("12345678-9");
        when(restTemplate.getForObject(contains("/usuarios/rut/12345678-9"), eq(UsuarioDTO.class)))
                .thenReturn(null);

        ResponseEntity<Object> response = apoderadoService.crearApoderado(dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void crearApoderado_perfilYaExistePorUsuarioId_retornaConflict() {
        ApoderadoDTO dto = new ApoderadoDTO();
        dto.setRut("12345678-9");
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId(1L);
        when(restTemplate.getForObject(contains("/usuarios/rut/12345678-9"), eq(UsuarioDTO.class)))
                .thenReturn(usuario);
        when(apoderadoRepository.existsByUsuarioId(1L)).thenReturn(true);

        ResponseEntity<Object> response = apoderadoService.crearApoderado(dto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void crearApoderado_perfilYaExistePorRut_retornaConflict() {
        ApoderadoDTO dto = new ApoderadoDTO();
        dto.setRut("12345678-9");
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId(1L);
        when(restTemplate.getForObject(contains("/usuarios/rut/12345678-9"), eq(UsuarioDTO.class)))
                .thenReturn(usuario);
        when(apoderadoRepository.existsByUsuarioId(1L)).thenReturn(false);
        when(apoderadoRepository.existsByRut("12345678-9")).thenReturn(true);

        ResponseEntity<Object> response = apoderadoService.crearApoderado(dto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void crearApoderado_exitoso_guardaYRetornaOk() {
        ApoderadoDTO dto = new ApoderadoDTO();
        dto.setRut("12345678-9");
        dto.setNombre("Carlos");
        dto.setApellido("Rojas");
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId(1L);
        when(restTemplate.getForObject(contains("/usuarios/rut/12345678-9"), eq(UsuarioDTO.class)))
                .thenReturn(usuario);
        when(apoderadoRepository.existsByUsuarioId(1L)).thenReturn(false);
        when(apoderadoRepository.existsByRut("12345678-9")).thenReturn(false);
        when(apoderadoRepository.save(any(Apoderado.class))).thenReturn(new Apoderado());

        ResponseEntity<Object> response = apoderadoService.crearApoderado(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(apoderadoRepository).save(any(Apoderado.class));
    }

    @Test
    void buscarPorUsuarioId_noExiste_retornaNull() {
        when(apoderadoRepository.findByUsuarioId(99L)).thenReturn(Optional.empty());

        ApoderadoDTO result = apoderadoService.buscarPorUsuarioId(99L);

        assertNull(result);
    }

    @Test
    void buscarPorUsuarioId_existe_retornaDTO() {
        Apoderado a = new Apoderado();
        a.setNombre("Ana");
        a.setApellido("Perez");
        a.setRut("99999999-9");
        when(apoderadoRepository.findByUsuarioId(1L)).thenReturn(Optional.of(a));

        ApoderadoDTO result = apoderadoService.buscarPorUsuarioId(1L);

        assertNotNull(result);
        assertEquals("Ana", result.getNombre());
    }

    @Test
    void asociar_alumnoNoExiste_retornaNotFound() {
        AsociarDTO dto = new AsociarDTO();
        dto.setRutAlumno("11111111-1");
        dto.setRutApoderado("22222222-2");
        when(alumnoRepository.findByRut("11111111-1")).thenReturn(Optional.empty());

        ResponseEntity<Object> response = apoderadoService.asociar(dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void asociar_apoderadoNoExiste_retornaNotFound() {
        AsociarDTO dto = new AsociarDTO();
        dto.setRutAlumno("11111111-1");
        dto.setRutApoderado("22222222-2");
        when(alumnoRepository.findByRut("11111111-1")).thenReturn(Optional.of(new Alumno()));
        when(apoderadoRepository.findByRut("22222222-2")).thenReturn(Optional.empty());

        ResponseEntity<Object> response = apoderadoService.asociar(dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void asociar_asociacionYaExiste_retornaConflict() {
        AsociarDTO dto = new AsociarDTO();
        dto.setRutAlumno("11111111-1");
        dto.setRutApoderado("22222222-2");
        Alumno alumno = new Alumno();
        Apoderado apoderado = new Apoderado();
        when(alumnoRepository.findByRut("11111111-1")).thenReturn(Optional.of(alumno));
        when(apoderadoRepository.findByRut("22222222-2")).thenReturn(Optional.of(apoderado));
        when(alumnoApoderadoRepository.existsByAlumnoAndApoderado(alumno, apoderado)).thenReturn(true);

        ResponseEntity<Object> response = apoderadoService.asociar(dto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void asociar_exitosa_guardaYRetornaOk() {
        AsociarDTO dto = new AsociarDTO();
        dto.setRutAlumno("11111111-1");
        dto.setRutApoderado("22222222-2");
        Alumno alumno = new Alumno();
        Apoderado apoderado = new Apoderado();
        when(alumnoRepository.findByRut("11111111-1")).thenReturn(Optional.of(alumno));
        when(apoderadoRepository.findByRut("22222222-2")).thenReturn(Optional.of(apoderado));
        when(alumnoApoderadoRepository.existsByAlumnoAndApoderado(alumno, apoderado)).thenReturn(false);
        when(alumnoApoderadoRepository.save(any(AlumnoApoderado.class))).thenReturn(new AlumnoApoderado());

        ResponseEntity<Object> response = apoderadoService.asociar(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(alumnoApoderadoRepository).save(any(AlumnoApoderado.class));
    }

    @Test
    void eliminarPorUsuarioId_llamaAlRepositorio() {
        apoderadoService.eliminarPorUsuarioId(1L);

        verify(apoderadoRepository).deleteByUsuarioId(1L);
    }

    @Test
    void listarAlumnosPorApoderado_apoderadoNoExiste_retornaListaVacia() {
        when(apoderadoRepository.findByUsuarioId(99L)).thenReturn(Optional.empty());

        var result = apoderadoService.listarAlumnosPorApoderado(99L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void listarAlumnosPorApoderado_sinAlumnos_retornaListaVacia() {
        Apoderado apoderado = new Apoderado();
        when(apoderadoRepository.findByUsuarioId(1L)).thenReturn(Optional.of(apoderado));
        when(alumnoApoderadoRepository.findByApoderado(apoderado)).thenReturn(Collections.emptyList());

        var result = apoderadoService.listarAlumnosPorApoderado(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

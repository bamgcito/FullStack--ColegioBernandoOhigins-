package com.pablo.ms_perfiles.service;

import com.pablo.ms_perfiles.dto.AlumnoDTO;
import com.pablo.ms_perfiles.dto.UsuarioDTO;
import com.pablo.ms_perfiles.model.Alumno;
import com.pablo.ms_perfiles.repository.AlumnoApoderadoRepository;
import com.pablo.ms_perfiles.repository.AlumnoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlumnoServiceTest {

    @Mock
    private AlumnoRepository alumnoRepository;

    @Mock
    private AlumnoApoderadoRepository alumnoApoderadoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AlumnoService alumnoService;

    @Test
    void crearAlumno_usuarioNoExiste_retornaNotFound() {
        AlumnoDTO dto = new AlumnoDTO();
        dto.setRut("12345678-9");
        when(restTemplate.getForObject(contains("/usuarios/rut/12345678-9"), eq(UsuarioDTO.class)))
                .thenReturn(null);

        ResponseEntity<Object> response = alumnoService.crearAlumno(dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void crearAlumno_perfilYaExistePorUsuarioId_retornaConflict() {
        AlumnoDTO dto = new AlumnoDTO();
        dto.setRut("12345678-9");
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId(1L);
        when(restTemplate.getForObject(contains("/usuarios/rut/12345678-9"), eq(UsuarioDTO.class)))
                .thenReturn(usuario);
        when(alumnoRepository.existsByUsuarioId(1L)).thenReturn(true);

        ResponseEntity<Object> response = alumnoService.crearAlumno(dto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void crearAlumno_perfilYaExistePorRut_retornaConflict() {
        AlumnoDTO dto = new AlumnoDTO();
        dto.setRut("12345678-9");
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId(1L);
        when(restTemplate.getForObject(contains("/usuarios/rut/12345678-9"), eq(UsuarioDTO.class)))
                .thenReturn(usuario);
        when(alumnoRepository.existsByUsuarioId(1L)).thenReturn(false);
        when(alumnoRepository.existsByRut("12345678-9")).thenReturn(true);

        ResponseEntity<Object> response = alumnoService.crearAlumno(dto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void crearAlumno_exitoso_guardaYRetornaOk() {
        AlumnoDTO dto = new AlumnoDTO();
        dto.setRut("12345678-9");
        dto.setNombre("Pedro");
        dto.setApellido("Soto");
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId(1L);
        when(restTemplate.getForObject(contains("/usuarios/rut/12345678-9"), eq(UsuarioDTO.class)))
                .thenReturn(usuario);
        when(alumnoRepository.existsByUsuarioId(1L)).thenReturn(false);
        when(alumnoRepository.existsByRut("12345678-9")).thenReturn(false);
        when(alumnoRepository.save(any(Alumno.class))).thenReturn(new Alumno());

        ResponseEntity<Object> response = alumnoService.crearAlumno(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(alumnoRepository).save(any(Alumno.class));
    }

    @Test
    void buscarPorUsuarioId_noExiste_retornaNull() {
        when(alumnoRepository.findByUsuarioId(99L)).thenReturn(Optional.empty());

        AlumnoDTO result = alumnoService.buscarPorUsuarioId(99L);

        assertNull(result);
    }

    @Test
    void buscarPorUsuarioId_existe_retornaDTO() {
        Alumno a = new Alumno();
        a.setNombre("Maria");
        a.setApellido("Gomez");
        a.setRut("33333333-3");
        when(alumnoRepository.findByUsuarioId(1L)).thenReturn(Optional.of(a));

        AlumnoDTO result = alumnoService.buscarPorUsuarioId(1L);

        assertNotNull(result);
        assertEquals("Maria", result.getNombre());
    }

    @Test
    void listarTodos_sinAlumnos_retornaListaVacia() {
        when(alumnoRepository.findAll()).thenReturn(Collections.emptyList());

        List<AlumnoDTO> result = alumnoService.listarTodos();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void listarTodos_conAlumnos_retornaLista() {
        Alumno a = new Alumno();
        a.setNombre("Luis");
        a.setApellido("Vera");
        a.setRut("44444444-4");
        when(alumnoRepository.findAll()).thenReturn(List.of(a));

        List<AlumnoDTO> result = alumnoService.listarTodos();

        assertEquals(1, result.size());
        assertEquals("Luis", result.get(0).getNombre());
    }

    @Test
    void listarApoderadosPorAlumno_alumnoNoExiste_retornaListaVacia() {
        when(alumnoRepository.findByUsuarioId(99L)).thenReturn(Optional.empty());

        var result = alumnoService.listarApoderadosPorAlumno(99L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void eliminarPorUsuarioId_llamaAlRepositorio() {
        alumnoService.eliminarPorUsuarioId(1L);

        verify(alumnoRepository).deleteByUsuarioId(1L);
    }
}

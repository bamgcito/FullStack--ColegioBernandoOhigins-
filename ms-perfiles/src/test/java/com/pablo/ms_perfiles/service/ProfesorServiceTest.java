package com.pablo.ms_perfiles.service;

import com.pablo.ms_perfiles.dto.ProfesorDTO;
import com.pablo.ms_perfiles.dto.UsuarioDTO;
import com.pablo.ms_perfiles.model.Profesor;
import com.pablo.ms_perfiles.repository.ProfesorRepository;
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
class ProfesorServiceTest {

    @Mock
    private ProfesorRepository profesorRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProfesorService profesorService;

    @Test
    void crearProfesor_usuarioNoExiste_retornaNotFound() {
        ProfesorDTO dto = new ProfesorDTO();
        dto.setRut("12345678-9");
        when(restTemplate.getForObject(contains("/usuarios/rut/12345678-9"), eq(UsuarioDTO.class)))
                .thenReturn(null);

        ResponseEntity<Object> response = profesorService.crearProfesor(dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void crearProfesor_perfilYaExistePorUsuarioId_retornaConflict() {
        ProfesorDTO dto = new ProfesorDTO();
        dto.setRut("12345678-9");
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId(1L);
        when(restTemplate.getForObject(contains("/usuarios/rut/12345678-9"), eq(UsuarioDTO.class)))
                .thenReturn(usuario);
        when(profesorRepository.existsByUsuarioId(1L)).thenReturn(true);

        ResponseEntity<Object> response = profesorService.crearProfesor(dto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void crearProfesor_perfilYaExistePorRut_retornaConflict() {
        ProfesorDTO dto = new ProfesorDTO();
        dto.setRut("12345678-9");
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId(1L);
        when(restTemplate.getForObject(contains("/usuarios/rut/12345678-9"), eq(UsuarioDTO.class)))
                .thenReturn(usuario);
        when(profesorRepository.existsByUsuarioId(1L)).thenReturn(false);
        when(profesorRepository.existsByRut("12345678-9")).thenReturn(true);

        ResponseEntity<Object> response = profesorService.crearProfesor(dto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void crearProfesor_exitoso_guardaYRetornaOk() {
        ProfesorDTO dto = new ProfesorDTO();
        dto.setRut("12345678-9");
        dto.setNombre("Juan");
        dto.setApellido("Pérez");
        dto.setEspecialidad("Matematicas");
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId(1L);
        when(restTemplate.getForObject(contains("/usuarios/rut/12345678-9"), eq(UsuarioDTO.class)))
                .thenReturn(usuario);
        when(profesorRepository.existsByUsuarioId(1L)).thenReturn(false);
        when(profesorRepository.existsByRut("12345678-9")).thenReturn(false);
        when(profesorRepository.save(any(Profesor.class))).thenReturn(new Profesor());

        ResponseEntity<Object> response = profesorService.crearProfesor(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(profesorRepository).save(any(Profesor.class));
    }

    @Test
    void buscarPorUsuarioId_noExiste_retornaNull() {
        when(profesorRepository.findByUsuarioId(99L)).thenReturn(Optional.empty());

        ProfesorDTO result = profesorService.buscarPorUsuarioId(99L);

        assertNull(result);
    }

    @Test
    void buscarPorUsuarioId_existe_retornaDTO() {
        Profesor p = new Profesor();
        p.setNombre("Ana");
        p.setApellido("Lopez");
        p.setRut("11111111-1");
        when(profesorRepository.findByUsuarioId(1L)).thenReturn(Optional.of(p));

        ProfesorDTO result = profesorService.buscarPorUsuarioId(1L);

        assertNotNull(result);
        assertEquals("Ana", result.getNombre());
    }

    @Test
    void buscarPorRut_noExiste_retornaNull() {
        when(profesorRepository.findByRut("99999999-9")).thenReturn(Optional.empty());

        ProfesorDTO result = profesorService.buscarPorRut("99999999-9");

        assertNull(result);
    }

    @Test
    void listarTodos_sinProfesores_retornaListaVacia() {
        when(profesorRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProfesorDTO> result = profesorService.listarTodos();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void listarTodos_conProfesores_retornaLista() {
        Profesor p = new Profesor();
        p.setNombre("Carlos");
        p.setApellido("Mora");
        p.setRut("22222222-2");
        when(profesorRepository.findAll()).thenReturn(List.of(p));

        List<ProfesorDTO> result = profesorService.listarTodos();

        assertEquals(1, result.size());
        assertEquals("Carlos", result.get(0).getNombre());
    }

    @Test
    void eliminarPorUsuarioId_llamaAlRepositorio() {
        profesorService.eliminarPorUsuarioId(1L);

        verify(profesorRepository).deleteByUsuarioId(1L);
    }
}

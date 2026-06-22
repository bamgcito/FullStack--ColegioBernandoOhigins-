package com.pablo.ms_cursos.service;

import com.pablo.ms_cursos.dto.CursoDTO;
import com.pablo.ms_cursos.model.Curso;
import com.pablo.ms_cursos.repository.CursoAlumnoRepository;
import com.pablo.ms_cursos.repository.CursoRepository;
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
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private CursoAlumnoRepository cursoAlumnoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CursoService cursoService;

    @Test
    void crearCurso_exitoso_retornaOk() {
        CursoDTO dto = new CursoDTO();
        dto.setNivel("1 Basico");
        dto.setLetra("A");
        dto.setAnio(2025);
        when(cursoRepository.save(any(Curso.class))).thenReturn(new Curso());

        ResponseEntity<Object> response = cursoService.crearCurso(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cursoRepository).save(any(Curso.class));
    }

    @Test
    void listarTodos_sinCursos_retornaListaVacia() {
        when(cursoRepository.findAll()).thenReturn(Collections.emptyList());

        List<CursoDTO> result = cursoService.listarTodos();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void listarTodos_conCursos_retornaLista() {
        Curso c = new Curso();
        c.setNivel("2 Basico");
        c.setLetra("B");
        c.setAnio(2025);
        when(cursoRepository.findAll()).thenReturn(List.of(c));
        when(cursoAlumnoRepository.countByCurso(c)).thenReturn(5L);

        List<CursoDTO> result = cursoService.listarTodos();

        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getAlumnosCount());
    }

    @Test
    void buscarPorId_noExiste_retornaNull() {
        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        CursoDTO result = cursoService.buscarPorId(99L);

        assertNull(result);
    }

    @Test
    void buscarPorId_existe_retornaDTO() {
        Curso c = new Curso();
        c.setNivel("3 Medio");
        c.setLetra("A");
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(c));

        CursoDTO result = cursoService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals("3 Medio", result.getNivel());
    }

    @Test
    void obtenerAlumnos_cursoNoExiste_retornaListaVacia() {
        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        var result = cursoService.obtenerAlumnos(99L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void obtenerAlumnos_sinAlumnos_retornaListaVacia() {
        Curso c = new Curso();
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(c));
        when(cursoAlumnoRepository.findByCurso(c)).thenReturn(Collections.emptyList());

        var result = cursoService.obtenerAlumnos(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void obtenerCursoPorAlumno_noExiste_retornaNull() {
        when(cursoAlumnoRepository.findByAlumnoId(99L)).thenReturn(Optional.empty());

        CursoDTO result = cursoService.obtenerCursoPorAlumno(99L);

        assertNull(result);
    }

    @Test
    void obtenerCursosPorProfesor_sinCursos_retornaListaVacia() {
        when(cursoRepository.findByProfesorJefeId(1L)).thenReturn(Collections.emptyList());

        List<CursoDTO> result = cursoService.obtenerCursosPorProfesor(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

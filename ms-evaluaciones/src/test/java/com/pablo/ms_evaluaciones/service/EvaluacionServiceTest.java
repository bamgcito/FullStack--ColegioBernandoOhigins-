package com.pablo.ms_evaluaciones.service;

import com.pablo.ms_evaluaciones.dto.AsignacionExternaDTO;
import com.pablo.ms_evaluaciones.dto.CrearEvaluacionDTO;
import com.pablo.ms_evaluaciones.dto.EvaluacionDTO;
import com.pablo.ms_evaluaciones.model.Evaluacion;
import com.pablo.ms_evaluaciones.repository.EvaluacionRepository;
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
class EvaluacionServiceTest {

    @Mock
    private EvaluacionRepository evaluacionRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EvaluacionService evaluacionService;

    @Test
    void crearEvaluacion_asignacionNoExiste_retornaNotFound() {
        CrearEvaluacionDTO dto = new CrearEvaluacionDTO();
        dto.setAsignacionId(99L);
        dto.setTitulo("Prueba 1");
        when(restTemplate.getForObject(contains("/asignaciones/99"), eq(AsignacionExternaDTO.class)))
                .thenReturn(null);

        ResponseEntity<Object> response = evaluacionService.crearEvaluacion(dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void crearEvaluacion_exitosa_guardaYRetornaOk() {
        CrearEvaluacionDTO dto = new CrearEvaluacionDTO();
        dto.setAsignacionId(1L);
        dto.setTitulo("Prueba 1");
        dto.setDescripcion("Primera prueba del semestre");
        when(restTemplate.getForObject(contains("/asignaciones/1"), eq(AsignacionExternaDTO.class)))
                .thenReturn(new AsignacionExternaDTO());
        when(evaluacionRepository.save(any(Evaluacion.class))).thenReturn(new Evaluacion());

        ResponseEntity<Object> response = evaluacionService.crearEvaluacion(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(evaluacionRepository).save(any(Evaluacion.class));
    }

    @Test
    void listarTodas_sinEvaluaciones_retornaListaVacia() {
        when(evaluacionRepository.findAll()).thenReturn(Collections.emptyList());

        List<EvaluacionDTO> result = evaluacionService.listarTodas();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void listarTodas_conEvaluaciones_retornaLista() {
        Evaluacion e = new Evaluacion();
        e.setTitulo("Control 1");
        e.setAsignacionId(1L);
        when(evaluacionRepository.findAll()).thenReturn(List.of(e));

        List<EvaluacionDTO> result = evaluacionService.listarTodas();

        assertEquals(1, result.size());
        assertEquals("Control 1", result.get(0).getTitulo());
    }

    @Test
    void buscarPorId_noExiste_retornaNotFound() {
        when(evaluacionRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = evaluacionService.buscarPorId(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void buscarPorId_existe_retornaOk() {
        Evaluacion e = new Evaluacion();
        e.setTitulo("Prueba Final");
        e.setAsignacionId(1L);
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(e));

        ResponseEntity<Object> response = evaluacionService.buscarPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void buscarPorAsignacion_sinResultados_retornaListaVacia() {
        when(evaluacionRepository.findByAsignacionId(1L)).thenReturn(Collections.emptyList());

        List<EvaluacionDTO> result = evaluacionService.buscarPorAsignacion(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void buscarPorAsignacion_conResultados_retornaLista() {
        Evaluacion e = new Evaluacion();
        e.setTitulo("Solemne 1");
        e.setAsignacionId(1L);
        when(evaluacionRepository.findByAsignacionId(1L)).thenReturn(List.of(e));

        List<EvaluacionDTO> result = evaluacionService.buscarPorAsignacion(1L);

        assertEquals(1, result.size());
    }
}

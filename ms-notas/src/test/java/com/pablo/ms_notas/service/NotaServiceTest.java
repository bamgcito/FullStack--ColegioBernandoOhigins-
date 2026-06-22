package com.pablo.ms_notas.service;

import com.pablo.ms_notas.dto.AlumnoPerfilDTO;
import com.pablo.ms_notas.dto.CrearNotaDTO;
import com.pablo.ms_notas.dto.EvaluacionExternaDTO;
import com.pablo.ms_notas.model.Nota;
import com.pablo.ms_notas.repository.NotaRepository;
import org.junit.jupiter.api.BeforeEach;
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
class NotaServiceTest {

    @Mock
    private NotaRepository notaRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NotaService notaService;

    private CrearNotaDTO dto;

    @BeforeEach
    void setUp() {
        dto = new CrearNotaDTO();
        dto.setAlumnoId(1L);
        dto.setEvaluacionId(1L);
    }

    @Test
    void registrarNota_valorNulo_retornaBadRequest() {
        dto.setValor(null);
        ResponseEntity<Object> response = notaService.registrarNota(dto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void registrarNota_valorMenorQueUno_retornaBadRequest() {
        dto.setValor(0.5);
        ResponseEntity<Object> response = notaService.registrarNota(dto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void registrarNota_valorMayorQueSiete_retornaBadRequest() {
        dto.setValor(7.5);
        ResponseEntity<Object> response = notaService.registrarNota(dto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void registrarNota_alumnoNoExiste_retornaNotFound() {
        dto.setValor(5.0);
        when(restTemplate.getForObject(contains("/perfiles/alumnos/1"), eq(AlumnoPerfilDTO.class)))
                .thenReturn(null);

        ResponseEntity<Object> response = notaService.registrarNota(dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void registrarNota_evaluacionNoExiste_retornaNotFound() {
        dto.setValor(5.0);
        when(restTemplate.getForObject(contains("/perfiles/alumnos/1"), eq(AlumnoPerfilDTO.class)))
                .thenReturn(new AlumnoPerfilDTO());
        when(restTemplate.getForObject(contains("/evaluaciones/1"), eq(EvaluacionExternaDTO.class)))
                .thenReturn(null);

        ResponseEntity<Object> response = notaService.registrarNota(dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void registrarNota_exitosa_guardaYRetornaOk() {
        dto.setValor(6.0);
        when(restTemplate.getForObject(contains("/perfiles/alumnos/1"), eq(AlumnoPerfilDTO.class)))
                .thenReturn(new AlumnoPerfilDTO());
        when(restTemplate.getForObject(contains("/evaluaciones/1"), eq(EvaluacionExternaDTO.class)))
                .thenReturn(new EvaluacionExternaDTO());
        when(notaRepository.findFirstByAlumnoIdAndEvaluacionId(1L, 1L))
                .thenReturn(Optional.empty());
        when(notaRepository.save(any(Nota.class))).thenReturn(new Nota());

        ResponseEntity<Object> response = notaService.registrarNota(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(notaRepository).save(any(Nota.class));
    }

    @Test
    void registrarNota_actualizaNotaExistente() {
        dto.setValor(4.5);
        Nota existente = new Nota();
        existente.setValor(3.0);
        when(restTemplate.getForObject(contains("/perfiles/alumnos/1"), eq(AlumnoPerfilDTO.class)))
                .thenReturn(new AlumnoPerfilDTO());
        when(restTemplate.getForObject(contains("/evaluaciones/1"), eq(EvaluacionExternaDTO.class)))
                .thenReturn(new EvaluacionExternaDTO());
        when(notaRepository.findFirstByAlumnoIdAndEvaluacionId(1L, 1L))
                .thenReturn(Optional.of(existente));
        when(notaRepository.save(any(Nota.class))).thenReturn(existente);

        ResponseEntity<Object> response = notaService.registrarNota(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4.5, existente.getValor());
    }

    @Test
    void calcularPromedio_sinNotas_retornaCero() {
        when(notaRepository.findByAlumnoId(1L)).thenReturn(Collections.emptyList());

        ResponseEntity<Object> response = notaService.calcularPromedio(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("0.0"));
    }

    @Test
    void calcularPromedio_conNotas_retornaPromedioCorrect() {
        Nota n1 = new Nota(); n1.setValor(6.0);
        Nota n2 = new Nota(); n2.setValor(4.0);
        when(notaRepository.findByAlumnoId(1L)).thenReturn(List.of(n1, n2));

        ResponseEntity<Object> response = notaService.calcularPromedio(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("5.0"));
    }

    @Test
    void obtenerPorAlumno_sinNotas_retornaListaVacia() {
        when(notaRepository.findByAlumnoId(1L)).thenReturn(Collections.emptyList());

        var result = notaService.obtenerPorAlumno(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void obtenerPorEvaluacion_sinNotas_retornaListaVacia() {
        when(notaRepository.findByEvaluacionId(1L)).thenReturn(Collections.emptyList());

        var result = notaService.obtenerPorEvaluacion(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void obtenerPorAlumnoYAsignacion_sinCoincidencia_retornaVacio() {
        when(notaRepository.findByAlumnoId(1L)).thenReturn(Collections.emptyList());

        var result = notaService.obtenerPorAlumnoYAsignacion(1L, 1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

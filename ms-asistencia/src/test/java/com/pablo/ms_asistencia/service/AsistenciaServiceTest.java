package com.pablo.ms_asistencia.service;

import com.pablo.ms_asistencia.dto.AlumnoPerfilDTO;
import com.pablo.ms_asistencia.dto.AsignacionExternaDTO;
import com.pablo.ms_asistencia.dto.CrearAsistenciaDTO;
import com.pablo.ms_asistencia.model.Asistencia;
import com.pablo.ms_asistencia.model.EstadoAsistencia;
import com.pablo.ms_asistencia.repository.AsistenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsistenciaServiceTest {

    @Mock
    private AsistenciaRepository asistenciaRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AsistenciaService asistenciaService;

    private CrearAsistenciaDTO dto;

    @BeforeEach
    void setUp() {
        dto = new CrearAsistenciaDTO();
        dto.setAlumnoId(1L);
        dto.setAsignacionId(1L);
        dto.setEstado(EstadoAsistencia.PRESENTE);
        dto.setFecha(LocalDate.now());
    }

    @Test
    void registrarAsistencia_alumnoNoExiste_retornaNotFound() {
        when(restTemplate.getForObject(contains("/perfiles/alumnos/1"), eq(AlumnoPerfilDTO.class)))
                .thenReturn(null);

        ResponseEntity<Object> response = asistenciaService.registrarAsistencia(dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void registrarAsistencia_asignacionNoExiste_retornaNotFound() {
        when(restTemplate.getForObject(contains("/perfiles/alumnos/1"), eq(AlumnoPerfilDTO.class)))
                .thenReturn(new AlumnoPerfilDTO());
        when(restTemplate.getForObject(contains("/asignaciones/1"), eq(AsignacionExternaDTO.class)))
                .thenReturn(null);

        ResponseEntity<Object> response = asistenciaService.registrarAsistencia(dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void registrarAsistencia_exitosa_guardaYRetornaOk() {
        when(restTemplate.getForObject(contains("/perfiles/alumnos/1"), eq(AlumnoPerfilDTO.class)))
                .thenReturn(new AlumnoPerfilDTO());
        when(restTemplate.getForObject(contains("/asignaciones/1"), eq(AsignacionExternaDTO.class)))
                .thenReturn(new AsignacionExternaDTO());
        when(asistenciaRepository.findByAlumnoIdAndAsignacionIdAndFecha(1L, 1L, dto.getFecha()))
                .thenReturn(Optional.empty());
        when(asistenciaRepository.save(any(Asistencia.class))).thenReturn(new Asistencia());

        ResponseEntity<Object> response = asistenciaService.registrarAsistencia(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(asistenciaRepository).save(any(Asistencia.class));
    }

    @Test
    void registrarAsistencia_actualizaRegistroExistente() {
        Asistencia existente = new Asistencia();
        existente.setEstado(EstadoAsistencia.AUSENTE);
        when(restTemplate.getForObject(contains("/perfiles/alumnos/1"), eq(AlumnoPerfilDTO.class)))
                .thenReturn(new AlumnoPerfilDTO());
        when(restTemplate.getForObject(contains("/asignaciones/1"), eq(AsignacionExternaDTO.class)))
                .thenReturn(new AsignacionExternaDTO());
        when(asistenciaRepository.findByAlumnoIdAndAsignacionIdAndFecha(1L, 1L, dto.getFecha()))
                .thenReturn(Optional.of(existente));
        when(asistenciaRepository.save(any(Asistencia.class))).thenReturn(existente);

        ResponseEntity<Object> response = asistenciaService.registrarAsistencia(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EstadoAsistencia.PRESENTE, existente.getEstado());
    }

    @Test
    void obtenerPorAlumno_sinRegistros_retornaListaVacia() {
        when(asistenciaRepository.findByAlumnoId(1L)).thenReturn(Collections.emptyList());

        var result = asistenciaService.obtenerPorAlumno(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void calcularPorcentaje_sinRegistros_retornaCero() {
        when(asistenciaRepository.findByAlumnoId(1L)).thenReturn(Collections.emptyList());

        ResponseEntity<Object> response = asistenciaService.calcularPorcentaje(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("0.0"));
    }

    @Test
    void calcularPorcentaje_todosPresentes_retornaCien() {
        Asistencia a1 = new Asistencia(); a1.setEstado(EstadoAsistencia.PRESENTE);
        Asistencia a2 = new Asistencia(); a2.setEstado(EstadoAsistencia.PRESENTE);
        when(asistenciaRepository.findByAlumnoId(1L)).thenReturn(List.of(a1, a2));

        ResponseEntity<Object> response = asistenciaService.calcularPorcentaje(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("100.0"));
    }

    @Test
    void calcularPorcentaje_mitadPresentes_retornaCincuenta() {
        Asistencia a1 = new Asistencia(); a1.setEstado(EstadoAsistencia.PRESENTE);
        Asistencia a2 = new Asistencia(); a2.setEstado(EstadoAsistencia.AUSENTE);
        when(asistenciaRepository.findByAlumnoId(1L)).thenReturn(List.of(a1, a2));

        ResponseEntity<Object> response = asistenciaService.calcularPorcentaje(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("50.0"));
    }

    @Test
    void obtenerPorAsignacion_sinRegistros_retornaListaVacia() {
        when(asistenciaRepository.findByAsignacionId(1L)).thenReturn(Collections.emptyList());

        var result = asistenciaService.obtenerPorAsignacion(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

package com.pablo.ms_asistencia.service;

import com.pablo.ms_asistencia.dto.AlumnoPerfilDTO;
import com.pablo.ms_asistencia.dto.AnotacionDTO;
import com.pablo.ms_asistencia.dto.AsignacionExternaDTO;
import com.pablo.ms_asistencia.dto.CrearAnotacionDTO;
import com.pablo.ms_asistencia.model.Anotacion;
import com.pablo.ms_asistencia.model.TipoAnotacion;
import com.pablo.ms_asistencia.repository.AnotacionRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnotacionServiceTest {

    @Mock
    private AnotacionRepository anotacionRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AnotacionService anotacionService;

    @Test
    void registrarAnotacion_alumnoNoExiste_retornaNotFound() {
        CrearAnotacionDTO dto = new CrearAnotacionDTO();
        dto.setAlumnoId(99L);
        dto.setAsignacionId(1L);
        when(restTemplate.getForObject(contains("/perfiles/alumnos/99"), eq(AlumnoPerfilDTO.class)))
                .thenReturn(null);

        ResponseEntity<Object> response = anotacionService.registrarAnotacion(dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void registrarAnotacion_asignacionNoExiste_retornaNotFound() {
        CrearAnotacionDTO dto = new CrearAnotacionDTO();
        dto.setAlumnoId(1L);
        dto.setAsignacionId(99L);
        when(restTemplate.getForObject(contains("/perfiles/alumnos/1"), eq(AlumnoPerfilDTO.class)))
                .thenReturn(new AlumnoPerfilDTO());
        when(restTemplate.getForObject(contains("/asignaciones/99"), eq(AsignacionExternaDTO.class)))
                .thenReturn(null);

        ResponseEntity<Object> response = anotacionService.registrarAnotacion(dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void registrarAnotacion_exitosa_guardaYRetornaOk() {
        CrearAnotacionDTO dto = new CrearAnotacionDTO();
        dto.setAlumnoId(1L);
        dto.setAsignacionId(1L);
        dto.setTipo(TipoAnotacion.POSITIVA);
        dto.setDescripcion("Excelente participacion");
        when(restTemplate.getForObject(contains("/perfiles/alumnos/1"), eq(AlumnoPerfilDTO.class)))
                .thenReturn(new AlumnoPerfilDTO());
        when(restTemplate.getForObject(contains("/asignaciones/1"), eq(AsignacionExternaDTO.class)))
                .thenReturn(new AsignacionExternaDTO());
        when(anotacionRepository.save(any(Anotacion.class))).thenReturn(new Anotacion());

        ResponseEntity<Object> response = anotacionService.registrarAnotacion(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(anotacionRepository).save(any(Anotacion.class));
    }

    @Test
    void obtenerPorAlumno_sinRegistros_retornaListaVacia() {
        when(anotacionRepository.findByAlumnoId(1L)).thenReturn(Collections.emptyList());

        List<AnotacionDTO> result = anotacionService.obtenerPorAlumno(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void obtenerPorAlumno_conRegistros_retornaLista() {
        Anotacion a = new Anotacion();
        a.setAlumnoId(1L);
        a.setAsignacionId(1L);
        a.setTipo(TipoAnotacion.NEGATIVA);
        a.setDescripcion("No trajo materiales");
        when(anotacionRepository.findByAlumnoId(1L)).thenReturn(List.of(a));

        List<AnotacionDTO> result = anotacionService.obtenerPorAlumno(1L);

        assertEquals(1, result.size());
    }

    @Test
    void obtenerPorAsignacion_sinRegistros_retornaListaVacia() {
        when(anotacionRepository.findByAsignacionId(1L)).thenReturn(Collections.emptyList());

        List<AnotacionDTO> result = anotacionService.obtenerPorAsignacion(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void obtenerPorAsignacion_conRegistros_retornaLista() {
        Anotacion a = new Anotacion();
        a.setAlumnoId(2L);
        a.setAsignacionId(1L);
        a.setTipo(TipoAnotacion.NEUTRA);
        a.setDescripcion("Observacion general");
        when(anotacionRepository.findByAsignacionId(1L)).thenReturn(List.of(a));

        List<AnotacionDTO> result = anotacionService.obtenerPorAsignacion(1L);

        assertEquals(1, result.size());
    }
}

package com.edutech.cl.main.service;

import com.edutech.cl.main.model.Evaluacion;
import com.edutech.cl.main.model.Curso;
import com.edutech.cl.main.repository.EvaluacionRepository;
import com.edutech.cl.main.repository.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluacionServiceTest {

    @Mock
    private EvaluacionRepository evaluacionRepository;

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private EvaluacionService evaluacionService;

    private Evaluacion evaluacion1;
    private Evaluacion evaluacion2;
    private Evaluacion evaluacionNueva;
    private Curso curso1;

    @BeforeEach
    void setUp() {
        curso1 = new Curso();
        curso1.setId(1L);
        curso1.setNombre("Java Programming");

        evaluacion1 = new Evaluacion();
        evaluacion1.setId(1L);
        evaluacion1.setTitulo("Examen Parcial");
        evaluacion1.setFecha(LocalDate.now());
        evaluacion1.setPuntajeMaximo(100);
        evaluacion1.setCurso(curso1);

        evaluacion2 = new Evaluacion();
        evaluacion2.setId(2L);
        evaluacion2.setTitulo("Examen Final");
        evaluacion2.setFecha(LocalDate.now().plusDays(7));
        evaluacion2.setPuntajeMaximo(150);
        evaluacion2.setCurso(curso1);

        evaluacionNueva = new Evaluacion();
        evaluacionNueva.setTitulo("Examen Parcial 2");
        evaluacionNueva.setFecha(LocalDate.now().plusDays(14));
        evaluacionNueva.setPuntajeMaximo(80);
        evaluacionNueva.setCurso(curso1);
    }

    @Test
    void testListar() {
        List<Evaluacion> evaluacionesEsperadas = Arrays.asList(evaluacion1, evaluacion2);
        when(evaluacionRepository.findAll()).thenReturn(evaluacionesEsperadas);

        List<Evaluacion> evaluaciones = evaluacionService.listar();

        assertNotNull(evaluaciones);
        assertEquals(2, evaluaciones.size());
        assertEquals("Examen Parcial", evaluaciones.get(0).getTitulo());
        assertEquals("Examen Final", evaluaciones.get(1).getTitulo());
        verify(evaluacionRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId() {
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion1));

        Evaluacion evaluacion = evaluacionService.obtenerPorId(1L);

        assertNotNull(evaluacion);
        assertEquals(1L, evaluacion.getId());
        assertEquals("Examen Parcial", evaluacion.getTitulo());
        verify(evaluacionRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_NotFound() {
        when(evaluacionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> evaluacionService.obtenerPorId(999L));
        verify(evaluacionRepository, times(1)).findById(999L);
    }

    @Test
    void testCrear() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso1));
        when(evaluacionRepository.save(any(Evaluacion.class))).thenReturn(evaluacion1);

        Evaluacion evaluacionCreada = evaluacionService.crear(evaluacionNueva, 1L);

        assertNotNull(evaluacionCreada);
        assertEquals(1L, evaluacionCreada.getId());
        assertEquals("Examen Parcial", evaluacionCreada.getTitulo());
        assertEquals(curso1, evaluacionNueva.getCurso());
        verify(cursoRepository, times(1)).findById(1L);
        verify(evaluacionRepository, times(1)).save(evaluacionNueva);
    }

    @Test
    void testCrear_CursoNoExiste() {
        when(cursoRepository.findById(999L)).thenReturn(Optional.empty());

        Evaluacion resultado = evaluacionService.crear(evaluacionNueva, 999L);

        assertNull(resultado);
        verify(cursoRepository, times(1)).findById(999L);
        verify(evaluacionRepository, never()).save(any());
    }

    @Test
    void testModificar() {
        Evaluacion evaluacionModificada = new Evaluacion();
        evaluacionModificada.setTitulo("Examen Parcial Actualizado");
        evaluacionModificada.setFecha(LocalDate.now().plusDays(5));
        evaluacionModificada.setPuntajeMaximo(120);
        evaluacionModificada.setCurso(curso1);

        Evaluacion evaluacionActualizada = new Evaluacion();
        evaluacionActualizada.setId(1L);
        evaluacionActualizada.setTitulo("Examen Parcial Actualizado");
        evaluacionActualizada.setFecha(LocalDate.now().plusDays(5));
        evaluacionActualizada.setPuntajeMaximo(120);
        evaluacionActualizada.setCurso(curso1);

        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion1));
        when(evaluacionRepository.save(any(Evaluacion.class))).thenReturn(evaluacionActualizada);

        Evaluacion resultado = evaluacionService.modificar(1L, evaluacionModificada);

        assertNotNull(resultado);
        assertEquals("Examen Parcial Actualizado", resultado.getTitulo());
        assertEquals(LocalDate.now().plusDays(5), resultado.getFecha());
        assertEquals(120, resultado.getPuntajeMaximo());
        verify(evaluacionRepository, times(1)).findById(1L);
        verify(evaluacionRepository, times(1)).save(any(Evaluacion.class));
    }

    @Test
    void testModificar_NotFound() {
        when(evaluacionRepository.findById(999L)).thenReturn(Optional.empty());

        Evaluacion resultado = evaluacionService.modificar(999L, evaluacionNueva);

        assertNull(resultado);
        verify(evaluacionRepository, times(1)).findById(999L);
        verify(evaluacionRepository, never()).save(any());
    }

    @Test
    void testEliminar() {
        doNothing().when(evaluacionRepository).deleteById(1L);

        evaluacionService.eliminar(1L);

        verify(evaluacionRepository, times(1)).deleteById(1L);
    }
} 

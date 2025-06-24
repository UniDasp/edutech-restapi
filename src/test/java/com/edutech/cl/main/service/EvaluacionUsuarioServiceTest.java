package com.edutech.cl.main.service;

import com.edutech.cl.main.model.EvaluacionUsuario;
import com.edutech.cl.main.model.Usuario;
import com.edutech.cl.main.model.Evaluacion;
import com.edutech.cl.main.repository.EvaluacionUsuarioRepository;
import com.edutech.cl.main.repository.UsuarioRepository;
import com.edutech.cl.main.repository.EvaluacionRepository;
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
class EvaluacionUsuarioServiceTest {

    @Mock
    private EvaluacionUsuarioRepository evaluacionUsuarioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EvaluacionRepository evaluacionRepository;

    @InjectMocks
    private EvaluacionUsuarioService evaluacionUsuarioService;

    private EvaluacionUsuario evaluacionUsuario1;
    private EvaluacionUsuario evaluacionUsuario2;
    private Usuario usuario1;
    private Usuario usuario2;
    private Evaluacion evaluacion1;
    private Evaluacion evaluacion2;
    private EvaluacionUsuario nuevaEvaluacionUsuario;

    @BeforeEach
    void setUp() {
        usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setUsername("juan.perez");

        usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setUsername("maria.garcia");

        evaluacion1 = new Evaluacion();
        evaluacion1.setId(1L);
        evaluacion1.setTitulo("Examen Parcial 1");

        evaluacion2 = new Evaluacion();
        evaluacion2.setId(2L);
        evaluacion2.setTitulo("Examen Final");

        evaluacionUsuario1 = new EvaluacionUsuario();
        evaluacionUsuario1.setId(1L);
        evaluacionUsuario1.setUsuario(usuario1);
        evaluacionUsuario1.setEvaluacion(evaluacion1);
        evaluacionUsuario1.setPuntajeObtenido(85);
        evaluacionUsuario1.setFechaEntrega(LocalDate.now());

        evaluacionUsuario2 = new EvaluacionUsuario();
        evaluacionUsuario2.setId(2L);
        evaluacionUsuario2.setUsuario(usuario2);
        evaluacionUsuario2.setEvaluacion(evaluacion2);
        evaluacionUsuario2.setPuntajeObtenido(92);
        evaluacionUsuario2.setFechaEntrega(LocalDate.now().plusDays(1));

        nuevaEvaluacionUsuario = new EvaluacionUsuario();
        nuevaEvaluacionUsuario.setPuntajeObtenido(78);
        nuevaEvaluacionUsuario.setFechaEntrega(LocalDate.now());
    }

    @Test
    void testListar() {
        List<EvaluacionUsuario> evaluacionesUsuarioEsperadas = Arrays.asList(evaluacionUsuario1, evaluacionUsuario2);
        when(evaluacionUsuarioRepository.findAll()).thenReturn(evaluacionesUsuarioEsperadas);

        List<EvaluacionUsuario> evaluacionesUsuario = evaluacionUsuarioService.listar();

        assertNotNull(evaluacionesUsuario);
        assertEquals(2, evaluacionesUsuario.size());
        assertEquals(85, evaluacionesUsuario.get(0).getPuntajeObtenido());
        assertEquals(92, evaluacionesUsuario.get(1).getPuntajeObtenido());
        verify(evaluacionUsuarioRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId() {
        when(evaluacionUsuarioRepository.findById(1L)).thenReturn(Optional.of(evaluacionUsuario1));

        EvaluacionUsuario evaluacionUsuario = evaluacionUsuarioService.obtenerPorId(1L);

        assertNotNull(evaluacionUsuario);
        assertEquals(1L, evaluacionUsuario.getId());
        assertEquals(85, evaluacionUsuario.getPuntajeObtenido());
        verify(evaluacionUsuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_NotFound() {
        when(evaluacionUsuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> evaluacionUsuarioService.obtenerPorId(999L));
        verify(evaluacionUsuarioRepository, times(1)).findById(999L);
    }

    @Test
    void testCrear() {
        nuevaEvaluacionUsuario.setUsuario(usuario1);
        nuevaEvaluacionUsuario.setEvaluacion(evaluacion1);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario1));
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion1));
        when(evaluacionUsuarioRepository.save(any(EvaluacionUsuario.class))).thenReturn(evaluacionUsuario1);

        EvaluacionUsuario evaluacionUsuarioCreada = evaluacionUsuarioService.crear(nuevaEvaluacionUsuario);

        assertNotNull(evaluacionUsuarioCreada);
        assertEquals(1L, evaluacionUsuarioCreada.getId());
        assertEquals(85, evaluacionUsuarioCreada.getPuntajeObtenido());
        assertEquals(usuario1, nuevaEvaluacionUsuario.getUsuario());
        assertEquals(evaluacion1, nuevaEvaluacionUsuario.getEvaluacion());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(evaluacionRepository, times(1)).findById(1L);
        verify(evaluacionUsuarioRepository, times(1)).save(nuevaEvaluacionUsuario);
    }

    @Test
    void testCrear_UsuarioNoExiste() {
        nuevaEvaluacionUsuario.setUsuario(usuario1);
        nuevaEvaluacionUsuario.setEvaluacion(evaluacion1);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> evaluacionUsuarioService.crear(nuevaEvaluacionUsuario));
        verify(usuarioRepository, times(1)).findById(1L);
        verify(evaluacionRepository, never()).findById(any());
        verify(evaluacionUsuarioRepository, never()).save(any());
    }

    @Test
    void testCrear_EvaluacionNoExiste() {
        nuevaEvaluacionUsuario.setUsuario(usuario1);
        nuevaEvaluacionUsuario.setEvaluacion(evaluacion1);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario1));
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> evaluacionUsuarioService.crear(nuevaEvaluacionUsuario));
        verify(usuarioRepository, times(1)).findById(1L);
        verify(evaluacionRepository, times(1)).findById(1L);
        verify(evaluacionUsuarioRepository, never()).save(any());
    }

    @Test
    void testModificar() {
        EvaluacionUsuario evaluacionUsuarioModificada = new EvaluacionUsuario();
        evaluacionUsuarioModificada.setUsuario(usuario2);
        evaluacionUsuarioModificada.setEvaluacion(evaluacion2);
        evaluacionUsuarioModificada.setPuntajeObtenido(95);
        evaluacionUsuarioModificada.setFechaEntrega(LocalDate.now().plusDays(2));

        EvaluacionUsuario evaluacionUsuarioActualizada = new EvaluacionUsuario();
        evaluacionUsuarioActualizada.setId(1L);
        evaluacionUsuarioActualizada.setUsuario(usuario2);
        evaluacionUsuarioActualizada.setEvaluacion(evaluacion2);
        evaluacionUsuarioActualizada.setPuntajeObtenido(95);
        evaluacionUsuarioActualizada.setFechaEntrega(LocalDate.now().plusDays(2));

        when(evaluacionUsuarioRepository.findById(1L)).thenReturn(Optional.of(evaluacionUsuario1));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario2));
        when(evaluacionRepository.findById(2L)).thenReturn(Optional.of(evaluacion2));
        when(evaluacionUsuarioRepository.save(any(EvaluacionUsuario.class))).thenReturn(evaluacionUsuarioActualizada);

        EvaluacionUsuario resultado = evaluacionUsuarioService.modificar(1L, evaluacionUsuarioModificada);

        assertNotNull(resultado);
        assertEquals(usuario2, resultado.getUsuario());
        assertEquals(evaluacion2, resultado.getEvaluacion());
        assertEquals(95, resultado.getPuntajeObtenido());
        assertEquals(LocalDate.now().plusDays(2), resultado.getFechaEntrega());
        verify(evaluacionUsuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).findById(2L);
        verify(evaluacionRepository, times(1)).findById(2L);
        verify(evaluacionUsuarioRepository, times(1)).save(any(EvaluacionUsuario.class));
    }

    @Test
    void testModificar_NotFound() {
        when(evaluacionUsuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> evaluacionUsuarioService.modificar(999L, nuevaEvaluacionUsuario));
        verify(evaluacionUsuarioRepository, times(1)).findById(999L);
        verify(usuarioRepository, never()).findById(any());
        verify(evaluacionRepository, never()).findById(any());
        verify(evaluacionUsuarioRepository, never()).save(any());
    }

    @Test
    void testEliminar() {
        doNothing().when(evaluacionUsuarioRepository).deleteById(1L);

        evaluacionUsuarioService.eliminar(1L);

        verify(evaluacionUsuarioRepository, times(1)).deleteById(1L);
    }
} 

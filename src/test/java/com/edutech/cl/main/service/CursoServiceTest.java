package com.edutech.cl.main.service;

import com.edutech.cl.main.model.Curso;
import com.edutech.cl.main.repository.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    private Curso curso1;
    private Curso curso2;
    private Curso cursoNuevo;

    @BeforeEach
    void setUp() {
        curso1 = new Curso();
        curso1.setId(1L);
        curso1.setNombre("Java Programming");
        curso1.setDescripcion("Curso completo de programación en Java");

        curso2 = new Curso();
        curso2.setId(2L);
        curso2.setNombre("Python Development");
        curso2.setDescripcion("Desarrollo de aplicaciones con Python");

        cursoNuevo = new Curso();
        cursoNuevo.setNombre("Web Development with React");
        cursoNuevo.setDescripcion("Desarrollo de aplicaciones web con React");
    }

    @Test
    void testListar() {
        List<Curso> cursosEsperados = Arrays.asList(curso1, curso2);
        when(cursoRepository.findAll()).thenReturn(cursosEsperados);

        List<Curso> cursos = cursoService.listar();

        assertNotNull(cursos);
        assertEquals(2, cursos.size());
        assertEquals("Java Programming", cursos.get(0).getNombre());
        assertEquals("Python Development", cursos.get(1).getNombre());
        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso1));

        Curso curso = cursoService.obtenerPorId(1L);

        assertNotNull(curso);
        assertEquals(1L, curso.getId());
        assertEquals("Java Programming", curso.getNombre());
        verify(cursoRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_NotFound() {
        when(cursoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cursoService.obtenerPorId(999L));
        verify(cursoRepository, times(1)).findById(999L);
    }

    @Test
    void testCrear() {
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso1);

        Curso cursoCreado = cursoService.crear(cursoNuevo);

        assertNotNull(cursoCreado);
        assertEquals(1L, cursoCreado.getId());
        assertEquals("Java Programming", cursoCreado.getNombre());
        verify(cursoRepository, times(1)).save(cursoNuevo);
    }

    @Test
    void testModificar() {
        Curso cursoModificado = new Curso();
        cursoModificado.setNombre("Java Programming Advanced");
        cursoModificado.setDescripcion("Curso avanzado de programación en Java");

        Curso cursoActualizado = new Curso();
        cursoActualizado.setId(1L);
        cursoActualizado.setNombre("Java Programming Advanced");
        cursoActualizado.setDescripcion("Curso avanzado de programación en Java");

        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso1));
        when(cursoRepository.save(any(Curso.class))).thenReturn(cursoActualizado);

        Curso resultado = cursoService.modificar(1L, cursoModificado);

        assertNotNull(resultado);
        assertEquals("Java Programming Advanced", resultado.getNombre());
        assertEquals("Curso avanzado de programación en Java", resultado.getDescripcion());
        verify(cursoRepository, times(1)).findById(1L);
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    void testModificar_NotFound() {
        when(cursoRepository.findById(999L)).thenReturn(Optional.empty());

        Curso resultado = cursoService.modificar(999L, cursoNuevo);

        assertNull(resultado);
        verify(cursoRepository, times(1)).findById(999L);
        verify(cursoRepository, never()).save(any(Curso.class));
    }

    @Test
    void testEliminar() {
        doNothing().when(cursoRepository).deleteById(1L);

        cursoService.eliminar(1L);

        verify(cursoRepository, times(1)).deleteById(1L);
    }
} 

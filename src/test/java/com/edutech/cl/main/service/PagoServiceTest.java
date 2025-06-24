package com.edutech.cl.main.service;

import com.edutech.cl.main.model.Curso;
import com.edutech.cl.main.model.Pago;
import com.edutech.cl.main.repository.PagoRepository;
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
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;

    private Pago pago1;
    private Pago pago2;
    private Pago pagoNuevo;
    private Curso curso1;

    @BeforeEach
    void setUp() {
        curso1 = new Curso();
        curso1.setId(1L);
        curso1.setNombre("Java Programming");

        pago1 = new Pago();
        pago1.setId(1L);
        pago1.setMonto(50000.0);
        pago1.setFecha(LocalDate.now());
        pago1.setMetodo("tarjeta");
        pago1.setEstado("PAGADO");
        pago1.setCurso(curso1);

        pago2 = new Pago();
        pago2.setId(2L);
        pago2.setMonto(75000.0);
        pago2.setFecha(LocalDate.now().plusDays(7));
        pago2.setMetodo("efectivo");
        pago2.setEstado("PENDIENTE");
        pago2.setCurso(curso1);

        pagoNuevo = new Pago();
        pagoNuevo.setMonto(60000.0);
        pagoNuevo.setFecha(LocalDate.now().plusDays(14));
        pagoNuevo.setMetodo("transferencia");
        pagoNuevo.setEstado("PENDIENTE");
        pagoNuevo.setCurso(curso1);
    }

    @Test
    void testListar() {
        List<Pago> pagosEsperados = Arrays.asList(pago1, pago2);
        when(pagoRepository.findAll()).thenReturn(pagosEsperados);

        List<Pago> pagos = pagoService.listar();

        assertNotNull(pagos);
        assertEquals(2, pagos.size());
        assertEquals(50000.0, pagos.get(0).getMonto());
        assertEquals(75000.0, pagos.get(1).getMonto());
        verify(pagoRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago1));

        Pago pago = pagoService.obtenerPorId(1L);

        assertNotNull(pago);
        assertEquals(1L, pago.getId());
        assertEquals(50000.0, pago.getMonto());
        verify(pagoRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_NotFound() {
        when(pagoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pagoService.obtenerPorId(999L));
        verify(pagoRepository, times(1)).findById(999L);
    }

    @Test
    void testCrear() {
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago1);

        Pago pagoCreado = pagoService.crear(pagoNuevo);

        assertNotNull(pagoCreado);
        assertEquals(1L, pagoCreado.getId());
        assertEquals(50000.0, pagoCreado.getMonto());
        verify(pagoRepository, times(1)).save(pagoNuevo);
    }

    @Test
    void testModificar() {
        Pago pagoModificado = new Pago();
        pagoModificado.setMonto(80000.0);
        pagoModificado.setFecha(LocalDate.now().plusDays(10));
        pagoModificado.setMetodo("tarjeta");
        pagoModificado.setEstado("PAGADO");
        pagoModificado.setCurso(curso1);

        Pago pagoActualizado = new Pago();
        pagoActualizado.setId(1L);
        pagoActualizado.setMonto(80000.0);
        pagoActualizado.setFecha(LocalDate.now().plusDays(10));
        pagoActualizado.setMetodo("tarjeta");
        pagoActualizado.setEstado("PAGADO");
        pagoActualizado.setCurso(curso1);

        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago1));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoActualizado);

        Pago resultado = pagoService.modificar(1L, pagoModificado);

        assertNotNull(resultado);
        assertEquals(80000.0, resultado.getMonto());
        assertEquals(LocalDate.now().plusDays(10), resultado.getFecha());
        assertEquals("PAGADO", resultado.getEstado());
        verify(pagoRepository, times(1)).findById(1L);
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void testModificar_NotFound() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.empty());

        Pago resultado = pagoService.modificar(1L, pagoNuevo);

        assertNull(resultado);
        verify(pagoRepository, times(1)).findById(1L);
        verify(pagoRepository, never()).save(any());
    }

    @Test
    void testEliminar() {
        doNothing().when(pagoRepository).deleteById(1L);

        pagoService.eliminar(1L);

        verify(pagoRepository, times(1)).deleteById(1L);
    }
} 

package com.edutech.cl.main.controller;

import com.edutech.cl.main.assembler.PagoAssembler;
import com.edutech.cl.main.model.Pago;
import com.edutech.cl.main.service.PagoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagoController.class)
class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PagoService pagoService;

    @MockBean
    private PagoAssembler pagoAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Pago pago1;
    private Pago pago2;
    private Pago pagoNuevo;
    private EntityModel<Pago> entityModel1;
    private EntityModel<Pago> entityModel2;

    @BeforeEach
    void setUp() {
        pago1 = new Pago();
        pago1.setId(1L);
        pago1.setMonto(50000.0);
        pago1.setFecha(LocalDate.now());
        pago1.setEstado("PAGADO");

        pago2 = new Pago();
        pago2.setId(2L);
        pago2.setMonto(75000.0);
        pago2.setFecha(LocalDate.now().plusDays(7));
        pago2.setEstado("PENDIENTE");

        pagoNuevo = new Pago();
        pagoNuevo.setMonto(60000.0);
        pagoNuevo.setFecha(LocalDate.now().plusDays(14));
        pagoNuevo.setEstado("PENDIENTE");

        entityModel1 = EntityModel.of(pago1);
        entityModel2 = EntityModel.of(pago2);
    }

    @Test
    void testListarPagos() throws Exception {
        List<Pago> pagos = Arrays.asList(pago1, pago2);
        when(pagoService.listar()).thenReturn(pagos);
        when(pagoAssembler.toCollectionModel(pagos)).thenReturn(
                org.springframework.hateoas.CollectionModel.of(Arrays.asList(entityModel1, entityModel2))
        );

        mockMvc.perform(get("/api/v1/pagos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.pagoList").exists())
                .andExpect(jsonPath("$._embedded.pagoList[0].monto").value(50000))
                .andExpect(jsonPath("$._embedded.pagoList[1].monto").value(75000));

        verify(pagoService, times(1)).listar();
    }

    @Test
    void testObtenerPagoPorId() throws Exception {
        when(pagoService.obtenerPorId(1L)).thenReturn(pago1);
        when(pagoAssembler.toModel(pago1)).thenReturn(entityModel1);

        mockMvc.perform(get("/api/v1/pagos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.monto").value(50000))
                .andExpect(jsonPath("$.estado").value("PAGADO"));

        verify(pagoService, times(1)).obtenerPorId(1L);
    }

    @Test
    void testObtenerPagoPorId_NotFound() throws Exception {
        when(pagoService.obtenerPorId(999L)).thenThrow(new RuntimeException("Pago no encontrado con id: 999"));

        mockMvc.perform(get("/api/v1/pagos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Pago no encontrado con id: 999"));

        verify(pagoService, times(1)).obtenerPorId(999L);
    }

    @Test
    void testCrearPago() throws Exception {
        when(pagoService.crear(any(Pago.class))).thenReturn(pago1);
        when(pagoAssembler.toModel(pago1)).thenReturn(entityModel1);

        mockMvc.perform(post("/api/v1/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagoNuevo)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.monto").value(50000));

        verify(pagoService, times(1)).crear(any(Pago.class));
    }

    @Test
    void testModificarPago() throws Exception {
        Pago pagoModificado = new Pago();
        pagoModificado.setMonto(80000.0);
        pagoModificado.setFecha(LocalDate.now().plusDays(10));
        pagoModificado.setEstado("PAGADO");

        Pago pagoActualizado = new Pago();
        pagoActualizado.setId(1L);
        pagoActualizado.setMonto(80000.0);
        pagoActualizado.setFecha(LocalDate.now().plusDays(10));
        pagoActualizado.setEstado("PAGADO");

        EntityModel<Pago> entityModelActualizado = EntityModel.of(pagoActualizado);

        when(pagoService.modificar(eq(1L), any(Pago.class))).thenReturn(pagoActualizado);
        when(pagoAssembler.toModel(pagoActualizado)).thenReturn(entityModelActualizado);

        mockMvc.perform(put("/api/v1/pagos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagoModificado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.monto").value(80000));

        verify(pagoService, times(1)).modificar(eq(1L), any(Pago.class));
        verify(pagoAssembler, times(1)).toModel(pagoActualizado);
    }

    @Test
    void testEliminarPago() throws Exception {
        doNothing().when(pagoService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/pagos/1"))
                .andExpect(status().isNoContent());

        verify(pagoService, times(1)).eliminar(1L);
    }
} 

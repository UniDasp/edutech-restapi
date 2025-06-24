package com.edutech.cl.main.controller;

import com.edutech.cl.main.assembler.EvaluacionAssembler;
import com.edutech.cl.main.model.Evaluacion;
import com.edutech.cl.main.service.EvaluacionService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EvaluacionController.class)
class EvaluacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EvaluacionService evaluacionService;

    @MockBean
    private EvaluacionAssembler evaluacionAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Evaluacion evaluacion1;
    private Evaluacion evaluacion2;
    private Evaluacion evaluacionNueva;
    private EntityModel<Evaluacion> entityModel1;
    private EntityModel<Evaluacion> entityModel2;

    @BeforeEach
    void setUp() {
        evaluacion1 = new Evaluacion();
        evaluacion1.setId(1L);
        evaluacion1.setTitulo("Java Fundamentals Exam");
        evaluacion1.setFecha(LocalDate.now());
        evaluacion1.setPuntajeMaximo(100);

        evaluacion2 = new Evaluacion();
        evaluacion2.setId(2L);
        evaluacion2.setTitulo("Python Final Project");
        evaluacion2.setFecha(LocalDate.now().plusDays(7));
        evaluacion2.setPuntajeMaximo(150);

        evaluacionNueva = new Evaluacion();
        evaluacionNueva.setTitulo("React Components Quiz");
        evaluacionNueva.setFecha(LocalDate.now().plusDays(14));
        evaluacionNueva.setPuntajeMaximo(80);

        entityModel1 = EntityModel.of(evaluacion1);
        entityModel2 = EntityModel.of(evaluacion2);
    }

    @Test
    void testListarEvaluaciones() throws Exception {
        List<Evaluacion> evaluaciones = Arrays.asList(evaluacion1, evaluacion2);
        when(evaluacionService.listar()).thenReturn(evaluaciones);
        when(evaluacionAssembler.toCollectionModel(evaluaciones)).thenReturn(
                org.springframework.hateoas.CollectionModel.of(Arrays.asList(entityModel1, entityModel2))
        );

        mockMvc.perform(get("/api/v1/evaluaciones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.evaluacionList").exists())
                .andExpect(jsonPath("$._embedded.evaluacionList[0].titulo").value("Java Fundamentals Exam"))
                .andExpect(jsonPath("$._embedded.evaluacionList[1].titulo").value("Python Final Project"));

        verify(evaluacionService, times(1)).listar();
    }

    @Test
    void testObtenerEvaluacionPorId() throws Exception {
        when(evaluacionService.obtenerPorId(1L)).thenReturn(evaluacion1);
        when(evaluacionAssembler.toModel(evaluacion1)).thenReturn(entityModel1);

        mockMvc.perform(get("/api/v1/evaluaciones/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.titulo").value("Java Fundamentals Exam"))
                .andExpect(jsonPath("$.puntajeMaximo").value(100));

        verify(evaluacionService, times(1)).obtenerPorId(1L);
    }

    @Test
    void testObtenerEvaluacionPorId_NotFound() throws Exception {
        when(evaluacionService.obtenerPorId(999L)).thenThrow(new RuntimeException("Evaluacion no encontrada con id: 999"));

        mockMvc.perform(get("/api/v1/evaluaciones/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Evaluacion no encontrada con id: 999"));

        verify(evaluacionService, times(1)).obtenerPorId(999L);
    }

    @Test
    void testCrearEvaluacion() throws Exception {
        when(evaluacionService.crear(any(Evaluacion.class), eq(1L))).thenReturn(evaluacion1);
        when(evaluacionAssembler.toModel(evaluacion1)).thenReturn(entityModel1);

        mockMvc.perform(post("/api/v1/evaluaciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evaluacionNueva)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.titulo").value("Java Fundamentals Exam"));

        verify(evaluacionService, times(1)).crear(any(Evaluacion.class), eq(1L));
    }

    @Test
    void testModificarEvaluacion() throws Exception {
        Evaluacion evaluacionModificada = new Evaluacion();
        evaluacionModificada.setTitulo("Java Fundamentals Exam Actualizado");
        evaluacionModificada.setFecha(LocalDate.now().plusDays(5));
        evaluacionModificada.setPuntajeMaximo(120);

        Evaluacion evaluacionActualizada = new Evaluacion();
        evaluacionActualizada.setId(1L);
        evaluacionActualizada.setTitulo("Java Fundamentals Exam Actualizado");
        evaluacionActualizada.setFecha(LocalDate.now().plusDays(5));
        evaluacionActualizada.setPuntajeMaximo(120);

        EntityModel<Evaluacion> entityModelActualizado = EntityModel.of(evaluacionActualizada);

        when(evaluacionService.modificar(eq(1L), any(Evaluacion.class))).thenReturn(evaluacionActualizada);
        when(evaluacionAssembler.toModel(evaluacionActualizada)).thenReturn(entityModelActualizado);

        mockMvc.perform(put("/api/v1/evaluaciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evaluacionModificada)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.titulo").value("Java Fundamentals Exam Actualizado"));

        verify(evaluacionService, times(1)).modificar(eq(1L), any(Evaluacion.class));
        verify(evaluacionAssembler, times(1)).toModel(evaluacionActualizada);
    }

    @Test
    void testEliminarEvaluacion() throws Exception {
        doNothing().when(evaluacionService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/evaluaciones/1"))
                .andExpect(status().isNoContent());

        verify(evaluacionService, times(1)).eliminar(1L);
    }
} 

package com.edutech.cl.main.controller;

import com.edutech.cl.main.assembler.EvaluacionUsuarioAssembler;
import com.edutech.cl.main.model.EvaluacionUsuario;
import com.edutech.cl.main.model.Usuario;
import com.edutech.cl.main.model.Evaluacion;
import com.edutech.cl.main.service.EvaluacionUsuarioService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EvaluacionUsuarioController.class)
class EvaluacionUsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EvaluacionUsuarioService evaluacionUsuarioService;

    @MockBean
    private EvaluacionUsuarioAssembler evaluacionUsuarioAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    private EvaluacionUsuario evaluacionUsuario1;
    private EvaluacionUsuario evaluacionUsuario2;
    private EvaluacionUsuario evaluacionUsuarioNueva;
    private Usuario usuario1;
    private Evaluacion evaluacion1;
    private EntityModel<EvaluacionUsuario> entityModel1;
    private EntityModel<EvaluacionUsuario> entityModel2;

    @BeforeEach
    void setUp() {
        usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setUsername("juan.perez");

        evaluacion1 = new Evaluacion();
        evaluacion1.setId(1L);
        evaluacion1.setTitulo("Java Fundamentals Exam");

        evaluacionUsuario1 = new EvaluacionUsuario();
        evaluacionUsuario1.setId(1L);
        evaluacionUsuario1.setUsuario(usuario1);
        evaluacionUsuario1.setEvaluacion(evaluacion1);
        evaluacionUsuario1.setPuntajeObtenido(85);
        evaluacionUsuario1.setFechaEntrega(LocalDate.now());

        evaluacionUsuario2 = new EvaluacionUsuario();
        evaluacionUsuario2.setId(2L);
        evaluacionUsuario2.setUsuario(usuario1);
        evaluacionUsuario2.setEvaluacion(evaluacion1);
        evaluacionUsuario2.setPuntajeObtenido(92);
        evaluacionUsuario2.setFechaEntrega(LocalDate.now().plusDays(1));

        evaluacionUsuarioNueva = new EvaluacionUsuario();
        evaluacionUsuarioNueva.setPuntajeObtenido(78);
        evaluacionUsuarioNueva.setFechaEntrega(LocalDate.now());
        evaluacionUsuarioNueva.setUsuario(usuario1);
        evaluacionUsuarioNueva.setEvaluacion(evaluacion1);

        entityModel1 = EntityModel.of(evaluacionUsuario1);
        entityModel2 = EntityModel.of(evaluacionUsuario2);
    }

    @Test
    void testListarEvaluacionesUsuario() throws Exception {
        List<EvaluacionUsuario> evaluacionesUsuario = Arrays.asList(evaluacionUsuario1, evaluacionUsuario2);
        when(evaluacionUsuarioService.listar()).thenReturn(evaluacionesUsuario);
        when(evaluacionUsuarioAssembler.toCollectionModel(evaluacionesUsuario)).thenReturn(
                org.springframework.hateoas.CollectionModel.of(Arrays.asList(entityModel1, entityModel2))
        );

        mockMvc.perform(get("/api/v1/evaluaciones-usuario"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.evaluacionUsuarioList").exists())
                .andExpect(jsonPath("$._embedded.evaluacionUsuarioList[0].puntajeObtenido").value(85))
                .andExpect(jsonPath("$._embedded.evaluacionUsuarioList[1].puntajeObtenido").value(92));

        verify(evaluacionUsuarioService, times(1)).listar();
    }

    @Test
    void testObtenerEvaluacionUsuarioPorId() throws Exception {
        when(evaluacionUsuarioService.obtenerPorId(1L)).thenReturn(evaluacionUsuario1);
        when(evaluacionUsuarioAssembler.toModel(evaluacionUsuario1)).thenReturn(entityModel1);

        mockMvc.perform(get("/api/v1/evaluaciones-usuario/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.puntajeObtenido").value(85))
                .andExpect(jsonPath("$.usuario.username").value("juan.perez"));

        verify(evaluacionUsuarioService, times(1)).obtenerPorId(1L);
    }

    @Test
    void testObtenerEvaluacionUsuarioPorId_NotFound() throws Exception {
        when(evaluacionUsuarioService.obtenerPorId(999L)).thenThrow(new RuntimeException("EvaluacionUsuario no encontrada con id: 999"));

        mockMvc.perform(get("/api/v1/evaluaciones-usuario/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("EvaluacionUsuario no encontrada con id: 999"));

        verify(evaluacionUsuarioService, times(1)).obtenerPorId(999L);
    }

    @Test
    void testCrearEvaluacionUsuario() throws Exception {
        when(evaluacionUsuarioService.crear(any(EvaluacionUsuario.class))).thenReturn(evaluacionUsuario1);
        when(evaluacionUsuarioAssembler.toModel(evaluacionUsuario1)).thenReturn(entityModel1);

        mockMvc.perform(post("/api/v1/evaluaciones-usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evaluacionUsuarioNueva)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.puntajeObtenido").value(85));

        verify(evaluacionUsuarioService, times(1)).crear(any(EvaluacionUsuario.class));
    }

    @Test
    void testModificarEvaluacionUsuario() throws Exception {
        EvaluacionUsuario evaluacionUsuarioModificada = new EvaluacionUsuario();
        evaluacionUsuarioModificada.setPuntajeObtenido(95);
        evaluacionUsuarioModificada.setFechaEntrega(LocalDate.now().plusDays(2));
        evaluacionUsuarioModificada.setUsuario(usuario1);
        evaluacionUsuarioModificada.setEvaluacion(evaluacion1);

        EvaluacionUsuario evaluacionUsuarioActualizada = new EvaluacionUsuario();
        evaluacionUsuarioActualizada.setId(1L);
        evaluacionUsuarioActualizada.setPuntajeObtenido(95);
        evaluacionUsuarioActualizada.setFechaEntrega(LocalDate.now().plusDays(2));
        evaluacionUsuarioActualizada.setUsuario(usuario1);
        evaluacionUsuarioActualizada.setEvaluacion(evaluacion1);

        EntityModel<EvaluacionUsuario> entityModelActualizado = EntityModel.of(evaluacionUsuarioActualizada);

        when(evaluacionUsuarioService.modificar(eq(1L), any(EvaluacionUsuario.class))).thenReturn(evaluacionUsuarioActualizada);
        when(evaluacionUsuarioAssembler.toModel(evaluacionUsuarioActualizada)).thenReturn(entityModelActualizado);

        mockMvc.perform(put("/api/v1/evaluaciones-usuario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evaluacionUsuarioModificada)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.puntajeObtenido").value(95));

        verify(evaluacionUsuarioService, times(1)).modificar(eq(1L), any(EvaluacionUsuario.class));
        verify(evaluacionUsuarioAssembler, times(1)).toModel(evaluacionUsuarioActualizada);
    }

    @Test
    void testEliminarEvaluacionUsuario() throws Exception {
        doNothing().when(evaluacionUsuarioService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/evaluaciones-usuario/1"))
                .andExpect(status().isNoContent());

        verify(evaluacionUsuarioService, times(1)).eliminar(1L);
    }
} 

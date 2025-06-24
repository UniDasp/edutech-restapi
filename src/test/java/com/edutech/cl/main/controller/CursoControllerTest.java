package com.edutech.cl.main.controller;

import com.edutech.cl.main.assembler.CursoAssembler;
import com.edutech.cl.main.model.Curso;
import com.edutech.cl.main.service.CursoService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CursoController.class)
class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CursoService cursoService;

    @MockBean
    private CursoAssembler cursoAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Curso curso1;
    private Curso curso2;
    private Curso cursoNuevo;
    private EntityModel<Curso> entityModel1;
    private EntityModel<Curso> entityModel2;

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

        entityModel1 = EntityModel.of(curso1);
        entityModel2 = EntityModel.of(curso2);
    }

    @Test
    void testListarCursos() throws Exception {
        List<Curso> cursos = Arrays.asList(curso1, curso2);
        when(cursoService.listar()).thenReturn(cursos);
        when(cursoAssembler.toCollectionModel(cursos)).thenReturn(
                org.springframework.hateoas.CollectionModel.of(Arrays.asList(entityModel1, entityModel2))
        );

        mockMvc.perform(get("/api/v1/cursos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.cursoList").exists())
                .andExpect(jsonPath("$._embedded.cursoList[0].nombre").value("Java Programming"))
                .andExpect(jsonPath("$._embedded.cursoList[1].nombre").value("Python Development"));

        verify(cursoService, times(1)).listar();
    }

    @Test
    void testObtenerCursoPorId() throws Exception {
        when(cursoService.obtenerPorId(1L)).thenReturn(curso1);
        when(cursoAssembler.toModel(curso1)).thenReturn(entityModel1);

        mockMvc.perform(get("/api/v1/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.nombre").value("Java Programming"))
                .andExpect(jsonPath("$.descripcion").value("Curso completo de programación en Java"));

        verify(cursoService, times(1)).obtenerPorId(1L);
    }

    @Test
    void testObtenerCursoPorId_NotFound() throws Exception {
        when(cursoService.obtenerPorId(999L)).thenThrow(new RuntimeException("Curso no encontrado con id: 999"));

        mockMvc.perform(get("/api/v1/cursos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Curso no encontrado con id: 999"));

        verify(cursoService, times(1)).obtenerPorId(999L);
    }

    @Test
    void testCrearCurso() throws Exception {
        when(cursoService.crear(any(Curso.class))).thenReturn(curso1);
        when(cursoAssembler.toModel(curso1)).thenReturn(entityModel1);

        mockMvc.perform(post("/api/v1/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoNuevo)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.nombre").value("Java Programming"));

        verify(cursoService, times(1)).crear(any(Curso.class));
    }

    @Test
    void testModificarCurso() throws Exception {
        Curso cursoModificado = new Curso();
        cursoModificado.setNombre("Java Programming Advanced");
        cursoModificado.setDescripcion("Curso avanzado de programación en Java");

        Curso cursoActualizado = new Curso();
        cursoActualizado.setId(1L);
        cursoActualizado.setNombre("Java Programming Advanced");
        cursoActualizado.setDescripcion("Curso avanzado de programación en Java");

        EntityModel<Curso> entityModelActualizado = EntityModel.of(cursoActualizado);

        when(cursoService.modificar(eq(1L), any(Curso.class))).thenReturn(cursoActualizado);
        when(cursoAssembler.toModel(cursoActualizado)).thenReturn(entityModelActualizado);

        mockMvc.perform(put("/api/v1/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoModificado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.nombre").value("Java Programming Advanced"));

        verify(cursoService, times(1)).modificar(eq(1L), any(Curso.class));
        verify(cursoAssembler, times(1)).toModel(cursoActualizado);
    }

    @Test
    void testModificarCurso_NotFound() throws Exception {
        Curso cursoModificado = new Curso();
        cursoModificado.setNombre("Java Programming Advanced");
        cursoModificado.setDescripcion("Curso avanzado de programación en Java");

        when(cursoService.modificar(eq(999L), any(Curso.class))).thenReturn(null);

        mockMvc.perform(put("/api/v1/cursos/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoModificado)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Curso no encontrado con id: 999"));

        verify(cursoService, times(1)).modificar(eq(999L), any(Curso.class));
    }

    @Test
    void testEliminarCurso() throws Exception {
        doNothing().when(cursoService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/cursos/1"))
                .andExpect(status().isNoContent());

        verify(cursoService, times(1)).eliminar(1L);
    }
} 

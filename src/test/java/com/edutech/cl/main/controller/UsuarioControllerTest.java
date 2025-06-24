package com.edutech.cl.main.controller;

import com.edutech.cl.main.assembler.UsuarioAssembler;
import com.edutech.cl.main.dto.request.UsuarioRequestDTO;
import com.edutech.cl.main.dto.response.UsuarioDTO;
import com.edutech.cl.main.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioAssembler usuarioAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioDTO usuarioDTO1;
    private UsuarioDTO usuarioDTO2;
    private UsuarioRequestDTO usuarioRequestDTO;
    private EntityModel<UsuarioDTO> entityModel1;
    private EntityModel<UsuarioDTO> entityModel2;
    private CollectionModel<EntityModel<UsuarioDTO>> collectionModel;

    @BeforeEach
    void setUp() {
        com.edutech.cl.main.model.Usuario usuario1 = new com.edutech.cl.main.model.Usuario();
        usuario1.setId(1L);
        usuario1.setUsername("juan.perez");
        usuario1.setPassword("123456");
        usuario1.setRol("USER");

        com.edutech.cl.main.model.Usuario usuario2 = new com.edutech.cl.main.model.Usuario();
        usuario2.setId(2L);
        usuario2.setUsername("maria.garcia");
        usuario2.setPassword("654321");
        usuario2.setRol("ADMIN");

        usuarioDTO1 = new UsuarioDTO(usuario1);
        usuarioDTO2 = new UsuarioDTO(usuario2);

        usuarioRequestDTO = new UsuarioRequestDTO();
        usuarioRequestDTO.setUsername("pedro.lopez");
        usuarioRequestDTO.setPassword("password123");
        usuarioRequestDTO.setRol("USER");

        entityModel1 = EntityModel.of(usuarioDTO1);
        entityModel2 = EntityModel.of(usuarioDTO2);
        collectionModel = CollectionModel.of(Arrays.asList(entityModel1, entityModel2));
    }

    @Test
    void testListarUsuarios() throws Exception {
        List<UsuarioDTO> usuarios = Arrays.asList(usuarioDTO1, usuarioDTO2);
        when(usuarioService.listar()).thenReturn(usuarios);
        when(usuarioAssembler.toCollectionModel(usuarios)).thenReturn(collectionModel);

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));

        verify(usuarioService, times(1)).listar();
        verify(usuarioAssembler, times(1)).toCollectionModel(usuarios);
    }

    @Test
    void testObtenerUsuarioPorId() throws Exception {
        when(usuarioService.obtenerPorId(1L)).thenReturn(usuarioDTO1);
        when(usuarioAssembler.toModel(usuarioDTO1)).thenReturn(entityModel1);

        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));

        verify(usuarioService, times(1)).obtenerPorId(1L);
        verify(usuarioAssembler, times(1)).toModel(usuarioDTO1);
    }

    @Test
    void testObtenerUsuarioPorId_NotFound() throws Exception {
        when(usuarioService.obtenerPorId(999L)).thenThrow(new RuntimeException("Usuario no encontrado con id: 999"));

        mockMvc.perform(get("/api/v1/usuarios/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Usuario no encontrado con id: 999"));

        verify(usuarioService, times(1)).obtenerPorId(999L);
    }

    @Test
    void testCrearUsuario() throws Exception {
        when(usuarioService.crear(any(UsuarioRequestDTO.class))).thenReturn(usuarioDTO1);
        when(usuarioAssembler.toModel(usuarioDTO1)).thenReturn(entityModel1);

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));

        verify(usuarioService, times(1)).crear(any(UsuarioRequestDTO.class));
        verify(usuarioAssembler, times(1)).toModel(usuarioDTO1);
    }

    @Test
    void testModificarUsuario() throws Exception {
        UsuarioRequestDTO usuarioRequestModificado = new UsuarioRequestDTO();
        usuarioRequestModificado.setUsername("juan.perez.actualizado");
        usuarioRequestModificado.setPassword("nuevaPassword");
        usuarioRequestModificado.setRol("ADMIN");

        com.edutech.cl.main.model.Usuario usuarioActualizado = new com.edutech.cl.main.model.Usuario();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setUsername("juan.perez.actualizado");
        usuarioActualizado.setPassword("nuevaPassword");
        usuarioActualizado.setRol("ADMIN");

        UsuarioDTO usuarioDTOActualizado = new UsuarioDTO(usuarioActualizado);
        EntityModel<UsuarioDTO> entityModelActualizado = EntityModel.of(usuarioDTOActualizado);

        when(usuarioService.modificar(eq(1L), any(UsuarioRequestDTO.class))).thenReturn(usuarioDTOActualizado);
        when(usuarioAssembler.toModel(usuarioDTOActualizado)).thenReturn(entityModelActualizado);

        mockMvc.perform(put("/api/v1/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequestModificado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));

        verify(usuarioService, times(1)).modificar(eq(1L), any(UsuarioRequestDTO.class));
        verify(usuarioAssembler, times(1)).toModel(usuarioDTOActualizado);
    }

    @Test
    void testModificarUsuario_NotFound() throws Exception {
        UsuarioRequestDTO usuarioRequestModificado = new UsuarioRequestDTO();
        usuarioRequestModificado.setUsername("juan.perez.actualizado");
        usuarioRequestModificado.setPassword("nuevaPassword");
        usuarioRequestModificado.setRol("ADMIN");

        when(usuarioService.modificar(eq(999L), any(UsuarioRequestDTO.class)))
                .thenThrow(new RuntimeException("Usuario no encontrado con id: 999"));

        mockMvc.perform(put("/api/v1/usuarios/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequestModificado)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Usuario no encontrado con id: 999"));

        verify(usuarioService, times(1)).modificar(eq(999L), any(UsuarioRequestDTO.class));
    }

    @Test
    void testEliminarUsuario() throws Exception {
        doNothing().when(usuarioService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).eliminar(1L);
    }
} 

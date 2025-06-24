package com.edutech.cl.main.service;

import com.edutech.cl.main.dto.request.UsuarioRequestDTO;
import com.edutech.cl.main.dto.response.UsuarioDTO;
import com.edutech.cl.main.model.Usuario;
import com.edutech.cl.main.repository.UsuarioRepository;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario1;
    private Usuario usuario2;
    private UsuarioRequestDTO usuarioRequestDTO;
    private UsuarioDTO usuarioDTO1;
    private UsuarioDTO usuarioDTO2;

    @BeforeEach
    void setUp() {
        usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setUsername("juan.perez");
        usuario1.setPassword("123456");
        usuario1.setRol("USER");

        usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setUsername("maria.garcia");
        usuario2.setPassword("654321");
        usuario2.setRol("ADMIN");

        usuarioRequestDTO = new UsuarioRequestDTO();
        usuarioRequestDTO.setUsername("pedro.lopez");
        usuarioRequestDTO.setPassword("password123");
        usuarioRequestDTO.setRol("USER");

        usuarioDTO1 = new UsuarioDTO(usuario1);
        usuarioDTO2 = new UsuarioDTO(usuario2);
    }

    @Test
    void testListarUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));

        List<UsuarioDTO> usuarios = usuarioService.listar();

        assertNotNull(usuarios);
        assertEquals(2, usuarios.size());
        assertEquals("juan.perez", usuarios.get(0).getUsername());
        assertEquals("maria.garcia", usuarios.get(1).getUsername());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario1));

        UsuarioDTO usuario = usuarioService.obtenerPorId(1L);

        assertNotNull(usuario);
        assertEquals(1L, usuario.getId());
        assertEquals("juan.perez", usuario.getUsername());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_NotFound() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.obtenerPorId(999L));
        verify(usuarioRepository, times(1)).findById(999L);
    }

    @Test
    void testCrear() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario1);

        UsuarioDTO usuarioCreado = usuarioService.crear(usuarioRequestDTO);

        assertNotNull(usuarioCreado);
        assertEquals(1L, usuarioCreado.getId());
        assertEquals("juan.perez", usuarioCreado.getUsername());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testModificar() {
        UsuarioRequestDTO usuarioRequestModificado = new UsuarioRequestDTO();
        usuarioRequestModificado.setUsername("juan.perez.actualizado");
        usuarioRequestModificado.setPassword("password123");
        usuarioRequestModificado.setRol("USER");

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setUsername("juan.perez.actualizado");
        usuarioActualizado.setPassword("password123");
        usuarioActualizado.setRol("USER");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario1));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioActualizado);

        UsuarioDTO resultado = usuarioService.modificar(1L, usuarioRequestModificado);

        assertNotNull(resultado);
        assertEquals("juan.perez.actualizado", resultado.getUsername());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testModificar_NotFound() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.modificar(999L, usuarioRequestDTO));
        verify(usuarioRepository, times(1)).findById(999L);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testActualizarRol() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario1));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario1);

        String resultado = usuarioService.actualizarRol(1L, "ADMIN");

        assertEquals("Rol actualizado correctamente", resultado);
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(usuario1);
    }

    @Test
    void testActualizarRol_NotFound() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        String resultado = usuarioService.actualizarRol(999L, "ADMIN");

        assertEquals("Usuario no encontrado", resultado);
        verify(usuarioRepository, times(1)).findById(999L);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testEliminar() {
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.eliminar(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }
} 

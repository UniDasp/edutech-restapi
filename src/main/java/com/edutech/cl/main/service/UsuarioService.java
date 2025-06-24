package com.edutech.cl.main.service;

import com.edutech.cl.main.dto.request.UsuarioRequestDTO;
import com.edutech.cl.main.dto.response.UsuarioDTO;
import com.edutech.cl.main.model.Usuario;
import com.edutech.cl.main.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioDTO::new)
                .collect(Collectors.toList());
    }

    public UsuarioDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        return new UsuarioDTO(usuario);
    }

    public UsuarioDTO crear(UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioRequestDTO.getUsername());
        usuario.setPassword(usuarioRequestDTO.getPassword());
        usuario.setRol(usuarioRequestDTO.getRol());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return new UsuarioDTO(usuarioGuardado);
    }

    public UsuarioDTO modificar(Long id, UsuarioRequestDTO usuarioRequestDTO) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setUsername(usuarioRequestDTO.getUsername());
            usuario.setPassword(usuarioRequestDTO.getPassword());
            usuario.setRol(usuarioRequestDTO.getRol());
            Usuario actualizado = usuarioRepository.save(usuario);
            return new UsuarioDTO(actualizado);
        }
        throw new RuntimeException("Usuario no encontrado con id: " + id);
    }

    public String actualizarRol(Long id, String nuevoRol) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setRol(nuevoRol);
            usuarioRepository.save(usuario);
            return "Rol actualizado correctamente";
        }
        return "Usuario no encontrado";
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}

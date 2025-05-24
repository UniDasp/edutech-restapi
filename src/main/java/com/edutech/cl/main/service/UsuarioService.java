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
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(UsuarioDTO::new)
                .collect(Collectors.toList());
    }

    public String crear(UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioRequestDTO.getUsername());
        usuario.setPassword(usuarioRequestDTO.getPassword());
        usuario.setRol(usuarioRequestDTO.getRol());

        usuarioRepository.save(usuario);
        return "Usuario registrado correctamente";
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

    public String eliminar(Long id) {
        usuarioRepository.deleteById(id);
        return "Usuario eliminado correctamente";
    }
}

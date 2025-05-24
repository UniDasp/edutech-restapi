package com.edutech.cl.main.dto.response;

import com.edutech.cl.main.model.Usuario;
import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String username;
    private String rol;

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.username = usuario.getUsername();
        this.rol = usuario.getRol();
    }
}
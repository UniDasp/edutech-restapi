package com.edutech.cl.main.dto.request;

import lombok.Data;

@Data
public class UsuarioRequestDTO {
    private Long id;
    private String username;
    private String password;
    private String rol;
}

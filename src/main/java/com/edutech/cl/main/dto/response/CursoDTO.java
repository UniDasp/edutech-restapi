package com.edutech.cl.main.dto.response;

import com.edutech.cl.main.model.Curso;
import lombok.Data;

@Data
public class CursoDTO {
    private Long id;
    private String nombre;
    private String descripcion;

    public CursoDTO(Curso curso){
        this.id = curso.getId();
        this.nombre = curso.getNombre();
        this.descripcion = curso.getDescripcion();
    }

}

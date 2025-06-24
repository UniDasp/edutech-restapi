package com.edutech.cl.main.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PagoRequestDTO {
    private Double monto;
    private LocalDate fecha;
    private String metodo;
    private String estado;
    private Long usuarioId;
    private Long cursoId;
}

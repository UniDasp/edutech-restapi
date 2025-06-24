package com.edutech.cl.main.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EvaluacionUsuarioRequestDTO {
    private Long usuarioId;
    private Long evaluacionId;
    private int puntajeObtenido;
    private LocalDate fechaEntrega;
} 
package com.edutech.cl.main.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EvaluacionRequestDTO {

    private String titulo;
    private LocalDate fecha;
    private int puntajeMaximo;
    private Long cursoId;
}

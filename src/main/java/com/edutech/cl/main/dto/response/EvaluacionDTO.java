package com.edutech.cl.main.dto.response;

import com.edutech.cl.main.model.Evaluacion;

import java.time.LocalDate;

public class EvaluacionDTO {

    private Long id;
    private String titulo;
    private LocalDate fecha;
    private int puntajeMaximo;
    private long cursoId;


    public EvaluacionDTO(Evaluacion evaluacion) {
        this.id = evaluacion.getId();
        this.titulo = evaluacion.getTitulo();
        this.fecha = evaluacion.getFecha();
        this.puntajeMaximo = evaluacion.getPuntajeMaximo();
        this.cursoId = evaluacion.getCurso() != null ? evaluacion.getCurso().getId() : null;
    }
}

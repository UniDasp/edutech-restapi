/*
package com.edutech.cl.main.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class EvaluacionUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Evaluacion evaluacion;

    private int puntajeObtenido;

    private LocalDate fechaEntrega;
}
*/
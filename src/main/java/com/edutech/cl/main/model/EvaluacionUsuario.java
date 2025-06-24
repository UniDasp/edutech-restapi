package com.edutech.cl.main.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "evaluacion_usuario")
@Data
public class EvaluacionUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonBackReference
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "evaluacion_id")
    @JsonBackReference
    private Evaluacion evaluacion;

    @Column(name = "puntaje_obtenido")
    private int puntajeObtenido;

    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;
}

package com.edutech.cl.main.controller;

import com.edutech.cl.main.model.Evaluacion;
import com.edutech.cl.main.service.EvaluacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/evaluaciones")
public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    public EvaluacionController(EvaluacionService evaluacionService) {
        this.evaluacionService = evaluacionService;
    }

    @GetMapping
    public List<Evaluacion> listar() {
        return evaluacionService.listar();
    }

    @PostMapping("/{cursoId}")
    public Evaluacion crear(@RequestBody Evaluacion evaluacion, @PathVariable Long cursoId) {
        return evaluacionService.crear(evaluacion, cursoId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        evaluacionService.eliminar(id);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}")
    public Evaluacion modificar(@PathVariable Long id, @RequestBody Evaluacion evaluacion) {
        return evaluacionService.modificar(id, evaluacion);
    }
}

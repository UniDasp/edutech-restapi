package com.edutech.cl.main.controller;

import com.edutech.cl.main.assembler.EvaluacionAssembler;
import com.edutech.cl.main.dto.request.EvaluacionRequestDTO;
import com.edutech.cl.main.model.Evaluacion;
import com.edutech.cl.main.service.EvaluacionService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/evaluaciones")
public class EvaluacionController {

    private final EvaluacionService evaluacionService;
    private final EvaluacionAssembler evaluacionAssembler;

    public EvaluacionController(EvaluacionService evaluacionService, EvaluacionAssembler evaluacionAssembler) {
        this.evaluacionService = evaluacionService;
        this.evaluacionAssembler = evaluacionAssembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Evaluacion>>> listar() {
        List<Evaluacion> evaluaciones = evaluacionService.listar();
        return ResponseEntity.ok(evaluacionAssembler.toCollectionModel(evaluaciones));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Evaluacion>> obtenerPorId(@PathVariable Long id) {
        Evaluacion evaluacion = evaluacionService.obtenerPorId(id);
        return ResponseEntity.ok(evaluacionAssembler.toModel(evaluacion));
    }

    @PostMapping("/{cursoId}")
    public ResponseEntity<EntityModel<Evaluacion>> crear(@RequestBody EvaluacionRequestDTO requestDTO, @PathVariable Long cursoId) {
        Evaluacion evaluacionCreada = evaluacionService.crear(requestDTO, cursoId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(evaluacionAssembler.toModel(evaluacionCreada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        evaluacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Evaluacion>> modificar(@PathVariable Long id, @RequestBody Evaluacion evaluacion) {
        Evaluacion evaluacionModificada = evaluacionService.modificar(id, evaluacion);
        return ResponseEntity.ok(evaluacionAssembler.toModel(evaluacionModificada));
    }
}

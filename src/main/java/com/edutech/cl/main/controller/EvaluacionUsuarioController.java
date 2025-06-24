package com.edutech.cl.main.controller;

import com.edutech.cl.main.assembler.EvaluacionUsuarioAssembler;
import com.edutech.cl.main.dto.request.EvaluacionUsuarioRequestDTO;
import com.edutech.cl.main.model.EvaluacionUsuario;
import com.edutech.cl.main.service.EvaluacionUsuarioService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/evaluaciones-usuarios")
public class EvaluacionUsuarioController {

    private final EvaluacionUsuarioService service;
    private final EvaluacionUsuarioAssembler evaluacionUsuarioAssembler;

    public EvaluacionUsuarioController(EvaluacionUsuarioService service, EvaluacionUsuarioAssembler evaluacionUsuarioAssembler) {
        this.service = service;
        this.evaluacionUsuarioAssembler = evaluacionUsuarioAssembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<EvaluacionUsuario>>> listar() {
        List<EvaluacionUsuario> evaluacionesUsuario = service.listar();
        return ResponseEntity.ok(evaluacionUsuarioAssembler.toCollectionModel(evaluacionesUsuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<EvaluacionUsuario>> obtenerPorId(@PathVariable Long id) {
        EvaluacionUsuario evaluacionUsuario = service.obtenerPorId(id);
        return ResponseEntity.ok(evaluacionUsuarioAssembler.toModel(evaluacionUsuario));
    }

    @PostMapping
    public ResponseEntity<EntityModel<EvaluacionUsuario>> crear(@RequestBody EvaluacionUsuarioRequestDTO requestDTO) {
        EvaluacionUsuario evaluacionUsuarioCreada = service.crear(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(evaluacionUsuarioAssembler.toModel(evaluacionUsuarioCreada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<EvaluacionUsuario>> modificar(@PathVariable Long id, @RequestBody EvaluacionUsuario nueva) {
        EvaluacionUsuario evaluacionUsuarioModificada = service.modificar(id, nueva);
        return ResponseEntity.ok(evaluacionUsuarioAssembler.toModel(evaluacionUsuarioModificada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

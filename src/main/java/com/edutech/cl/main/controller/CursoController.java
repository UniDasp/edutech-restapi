package com.edutech.cl.main.controller;

import com.edutech.cl.main.assembler.CursoAssembler;
import com.edutech.cl.main.dto.request.CursoRequestDTO;
import com.edutech.cl.main.model.Curso;
import com.edutech.cl.main.service.CursoService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cursos")
public class CursoController {

    private final CursoService cursoService;
    private final CursoAssembler cursoAssembler;

    public CursoController(CursoService cursoService, CursoAssembler cursoAssembler) {
        this.cursoService = cursoService;
        this.cursoAssembler = cursoAssembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Curso>>> listar() {
        List<Curso> cursos = cursoService.listar();
        return ResponseEntity.ok(cursoAssembler.toCollectionModel(cursos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Curso>> obtenerPorId(@PathVariable Long id) {
        Curso curso = cursoService.obtenerPorId(id);
        return ResponseEntity.ok(cursoAssembler.toModel(curso));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Curso>> crear(@RequestBody CursoRequestDTO requestDTO) {
        Curso cursoCreado = cursoService.crear(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cursoAssembler.toModel(cursoCreado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cursoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Curso>> modificar(@PathVariable Long id, @RequestBody Curso curso) {
        Curso cursoModificado = cursoService.modificar(id, curso);
        if (cursoModificado != null) {
            return ResponseEntity.ok(cursoAssembler.toModel(cursoModificado));
        } else {
            throw new RuntimeException("Curso no encontrado con id: " + id);
        }
    }
}

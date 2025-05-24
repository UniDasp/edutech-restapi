package com.edutech.cl.main.controller;

import com.edutech.cl.main.model.Curso;
import com.edutech.cl.main.service.CursoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public List<Curso> listar() {
        return cursoService.listar();
    }

    @PostMapping
    public Curso crear(@RequestBody Curso curso) {
        return cursoService.crear(curso);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cursoService.eliminar(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public Curso modificar(@PathVariable Long id, @RequestBody Curso curso) {
        return cursoService.modificar(id, curso);
    }
}

package com.edutech.cl.main.controller;

import com.edutech.cl.main.model.EvaluacionUsuario;
import com.edutech.cl.main.service.EvaluacionUsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/evaluaciones-usuario")
public class EvaluacionUsuarioController {

    private final EvaluacionUsuarioService service;

    public EvaluacionUsuarioController(EvaluacionUsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<EvaluacionUsuario> listar() {
        return service.listar();
    }

    @PostMapping
    public EvaluacionUsuario crear(@RequestBody EvaluacionUsuario evu) {
        return service.crear(evu);
    }

    @PutMapping("/{id}")
    public EvaluacionUsuario modificar(@PathVariable Long id, @RequestBody EvaluacionUsuario nueva) {
        return service.modificar(id, nueva);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}

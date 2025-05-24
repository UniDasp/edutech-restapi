package com.edutech.cl.main.controller;

import com.edutech.cl.main.model.Usuario;
import com.edutech.cl.main.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService UsuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.UsuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> listar() {
        return UsuarioService.listar();
    }

    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        return UsuarioService.crear(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        UsuarioService.eliminar(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public Usuario modificar(@PathVariable Long id, @RequestBody Usuario usuario) {
        return UsuarioService.modificar(id, usuario);
    }
}
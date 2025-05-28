package com.edutech.cl.main.controller;

import com.edutech.cl.main.dto.request.UsuarioRequestDTO;
import com.edutech.cl.main.dto.response.UsuarioDTO;
import com.edutech.cl.main.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> crear(@RequestBody UsuarioRequestDTO usuarioDto) {
        return ResponseEntity.ok(usuarioService.crear(usuarioDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> modificar(@PathVariable Long id,
                                                @RequestBody UsuarioRequestDTO usuarioDto) {
        return ResponseEntity.ok(usuarioService.modificar(id, usuarioDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        String mensaje = usuarioService.eliminar(id);
        return ResponseEntity.ok(mensaje);
    }
}

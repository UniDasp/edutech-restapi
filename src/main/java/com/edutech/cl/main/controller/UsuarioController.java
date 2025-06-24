package com.edutech.cl.main.controller;

import com.edutech.cl.main.assembler.UsuarioAssembler;
import com.edutech.cl.main.dto.request.UsuarioRequestDTO;
import com.edutech.cl.main.dto.response.UsuarioDTO;
import com.edutech.cl.main.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "API para gestión de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioAssembler usuarioAssembler;

    public UsuarioController(UsuarioService usuarioService, UsuarioAssembler usuarioAssembler) {
        this.usuarioService = usuarioService;
        this.usuarioAssembler = usuarioAssembler;
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Obtiene una lista de todos los usuarios registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<UsuarioDTO>>> listar() {
        List<UsuarioDTO> usuarios = usuarioService.listar();
        return ResponseEntity.ok(usuarioAssembler.toCollectionModel(usuarios));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Obtiene un usuario específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<EntityModel<UsuarioDTO>> obtenerPorId(
            @Parameter(description = "ID del usuario a buscar") @PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(usuarioAssembler.toModel(usuario));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario", description = "Crea un nuevo usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<EntityModel<UsuarioDTO>> crear(
            @Parameter(description = "Datos del usuario a crear") @RequestBody UsuarioRequestDTO usuarioDto) {
        UsuarioDTO usuarioCreado = usuarioService.crear(usuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioAssembler.toModel(usuarioCreado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar usuario", description = "Modifica un usuario existente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario modificado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<EntityModel<UsuarioDTO>> modificar(
            @Parameter(description = "ID del usuario a modificar") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del usuario") @RequestBody UsuarioRequestDTO usuarioDto) {
        UsuarioDTO usuarioModificado = usuarioService.modificar(id, usuarioDto);
        return ResponseEntity.ok(usuarioAssembler.toModel(usuarioModificado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del usuario a eliminar") @PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

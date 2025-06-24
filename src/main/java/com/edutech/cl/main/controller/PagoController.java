package com.edutech.cl.main.controller;

import com.edutech.cl.main.assembler.PagoAssembler;
import com.edutech.cl.main.dto.request.PagoRequestDTO;
import com.edutech.cl.main.model.Pago;
import com.edutech.cl.main.service.PagoService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    private final PagoService pagoService;
    private final PagoAssembler pagoAssembler;

    public PagoController(PagoService pagoService, PagoAssembler pagoAssembler) {
        this.pagoService = pagoService;
        this.pagoAssembler = pagoAssembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> listar() {
        List<Pago> pagos = pagoService.listar();
        return ResponseEntity.ok(pagoAssembler.toCollectionModel(pagos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pago>> obtenerPorId(@PathVariable Long id) {
        Pago pago = pagoService.obtenerPorId(id);
        return ResponseEntity.ok(pagoAssembler.toModel(pago));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Pago>> crear(@RequestBody PagoRequestDTO requestDTO) {
        Pago pagoCreado = pagoService.crear(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pagoAssembler.toModel(pagoCreado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Pago>> modificar(@PathVariable Long id, @RequestBody Pago pago) {
        Pago pagoModificado = pagoService.modificar(id, pago);
        return ResponseEntity.ok(pagoAssembler.toModel(pagoModificado));
    }
}

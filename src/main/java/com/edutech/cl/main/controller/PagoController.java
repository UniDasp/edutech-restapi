package com.edutech.cl.main.controller;

import com.edutech.cl.main.model.Pago;
import com.edutech.cl.main.service.PagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public List<Pago> listar() {
        return pagoService.listar();
    }

    @PostMapping
    public Pago crear(@RequestBody Pago pago) {
        return pagoService.crear(pago);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public Pago modificar(@PathVariable Long id, @RequestBody Pago pago) {
        return pagoService.modificar(id, pago);
    }
}

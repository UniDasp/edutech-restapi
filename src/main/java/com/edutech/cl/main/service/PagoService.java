package com.edutech.cl.main.service;

import com.edutech.cl.main.model.Pago;
import com.edutech.cl.main.repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public List<Pago> listar() {
        return pagoRepository.findAll();
    }

    public Pago crear(Pago pago) {
        return pagoRepository.save(pago);
    }

    public void eliminar(Long id) {
        pagoRepository.deleteById(id);
    }

    public Pago modificar(Long id, Pago nuevo) {
        Optional<Pago> existente = pagoRepository.findById(id);
        if (existente.isPresent()) {
            Pago pago = existente.get();
            pago.setMonto(nuevo.getMonto());
            pago.setFecha(nuevo.getFecha());
            pago.setMetodo(nuevo.getMetodo());
            pago.setEstado(nuevo.getEstado());

            return pagoRepository.save(pago);
        }
        return null;
    }
}

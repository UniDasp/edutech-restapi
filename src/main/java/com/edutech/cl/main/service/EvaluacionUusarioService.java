/*
package com.edutech.cl.main.service;

import com.edutech.cl.main.model.EvaluacionUsuario;
import com.edutech.cl.main.repository.EvaluacionUsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluacionUsuarioService {

    private final EvaluacionUsuarioRepository repository;

    public EvaluacionUsuarioService(EvaluacionUsuarioRepository repository) {
        this.repository = repository;
    }

    public List<EvaluacionUsuario> listar() {
        return repository.findAll();
    }

    public EvaluacionUsuario crear(EvaluacionUsuario evaluacionUsuario) {
        return repository.save(evaluacionUsuario);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public EvaluacionUsuario modificar(Long id, EvaluacionUsuario nueva) {
        EvaluacionUsuario existente = repository.findById(id).orElseThrow();
        existente.setEvaluacion(nueva.getEvaluacion());
        existente.setUsuario(nueva.getUsuario());
        existente.setPuntajeObtenido(nueva.getPuntajeObtenido());
        existente.setFechaEntrega(nueva.getFechaEntrega());
        return repository.save(existente);
    }
}
*/
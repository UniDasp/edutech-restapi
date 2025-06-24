package com.edutech.cl.main.service;

import com.edutech.cl.main.dto.request.EvaluacionUsuarioRequestDTO;
import com.edutech.cl.main.model.EvaluacionUsuario;
import com.edutech.cl.main.model.Usuario;
import com.edutech.cl.main.model.Evaluacion;
import com.edutech.cl.main.repository.EvaluacionUsuarioRepository;
import com.edutech.cl.main.repository.UsuarioRepository;
import com.edutech.cl.main.repository.EvaluacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluacionUsuarioService {

    private final EvaluacionUsuarioRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final EvaluacionRepository evaluacionRepository;

    public EvaluacionUsuarioService(EvaluacionUsuarioRepository repository, UsuarioRepository usuarioRepository, EvaluacionRepository evaluacionRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.evaluacionRepository = evaluacionRepository;
    }

    public List<EvaluacionUsuario> listar() {
        return repository.findAll();
    }

    public EvaluacionUsuario obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("EvaluacionUsuario no encontrada con id: " + id));
    }

    public EvaluacionUsuario crear(EvaluacionUsuarioRequestDTO requestDTO) {
        Usuario usuario = usuarioRepository.findById(requestDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + requestDTO.getUsuarioId()));

        Evaluacion evaluacion = evaluacionRepository.findById(requestDTO.getEvaluacionId())
                .orElseThrow(() -> new RuntimeException("Evaluacion no encontrada con id: " + requestDTO.getEvaluacionId()));

        EvaluacionUsuario evaluacionUsuario = new EvaluacionUsuario();
        evaluacionUsuario.setUsuario(usuario);
        evaluacionUsuario.setEvaluacion(evaluacion);
        evaluacionUsuario.setPuntajeObtenido(requestDTO.getPuntajeObtenido());
        evaluacionUsuario.setFechaEntrega(requestDTO.getFechaEntrega());

        return repository.save(evaluacionUsuario);
    }

    public EvaluacionUsuario crear(EvaluacionUsuario evaluacionUsuario) {
        Usuario usuario = usuarioRepository.findById(evaluacionUsuario.getUsuario().getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Evaluacion evaluacion = evaluacionRepository.findById(evaluacionUsuario.getEvaluacion().getId()).orElseThrow(() -> new RuntimeException("Evaluacion no encontrada"));

        evaluacionUsuario.setUsuario(usuario);
        evaluacionUsuario.setEvaluacion(evaluacion);

        return repository.save(evaluacionUsuario);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public EvaluacionUsuario modificar(Long id, EvaluacionUsuario nueva) {
        EvaluacionUsuario existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("EvaluacionUsuario no encontrada"));

        Usuario usuario = usuarioRepository.findById(nueva.getUsuario().getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Evaluacion evaluacion = evaluacionRepository.findById(nueva.getEvaluacion().getId()).orElseThrow(() -> new RuntimeException("Evaluacion no encontrada"));

        existente.setUsuario(usuario);
        existente.setEvaluacion(evaluacion);
        existente.setPuntajeObtenido(nueva.getPuntajeObtenido());
        existente.setFechaEntrega(nueva.getFechaEntrega());

        return repository.save(existente);
    }
}

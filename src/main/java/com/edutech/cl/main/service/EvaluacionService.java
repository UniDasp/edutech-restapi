package com.edutech.cl.main.service;

import com.edutech.cl.main.dto.request.EvaluacionRequestDTO;
import com.edutech.cl.main.model.Evaluacion;
import com.edutech.cl.main.model.Curso;
import com.edutech.cl.main.repository.EvaluacionRepository;
import com.edutech.cl.main.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;
    private final CursoRepository cursoRepository;

    public EvaluacionService(EvaluacionRepository evaluacionRepository, CursoRepository cursoRepository) {
        this.evaluacionRepository = evaluacionRepository;
        this.cursoRepository = cursoRepository;
    }

    public List<Evaluacion> listar() {
        return evaluacionRepository.findAll();
    }

    public Evaluacion obtenerPorId(Long id) {
        return evaluacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluacion no encontrada con id: " + id));
    }

    public Evaluacion crear(EvaluacionRequestDTO requestDTO, Long cursoId) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con id: " + cursoId));

        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setTitulo(requestDTO.getTitulo());
        evaluacion.setFecha(requestDTO.getFecha());
        evaluacion.setPuntajeMaximo(requestDTO.getPuntajeMaximo());
        evaluacion.setCurso(curso);

        return evaluacionRepository.save(evaluacion);
    }

    public Evaluacion crear(Evaluacion evaluacion, Long cursoId) {
        Optional<Curso> curso = cursoRepository.findById(cursoId);
        if (curso.isPresent()) {
            evaluacion.setCurso(curso.get());
            return evaluacionRepository.save(evaluacion);
        }
        return null;
    }

    public void eliminar(Long id) {
        evaluacionRepository.deleteById(id);
    }

    public Evaluacion modificar(Long id, Evaluacion nuevaEvaluacion) {
        Optional<Evaluacion> evalExistente = evaluacionRepository.findById(id);
        if (evalExistente.isPresent()) {
            Evaluacion e = evalExistente.get();
            e.setTitulo(nuevaEvaluacion.getTitulo());
            e.setFecha(nuevaEvaluacion.getFecha());
            e.setPuntajeMaximo(nuevaEvaluacion.getPuntajeMaximo());
            return evaluacionRepository.save(e);
        }
        return null;
    }
}

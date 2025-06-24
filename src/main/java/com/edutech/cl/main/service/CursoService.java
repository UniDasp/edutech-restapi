package com.edutech.cl.main.service;

import com.edutech.cl.main.dto.request.CursoRequestDTO;
import com.edutech.cl.main.model.Curso;
import com.edutech.cl.main.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<Curso> listar() {
        return cursoRepository.findAll();
    }

    public Curso obtenerPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con id: " + id));
    }

    public Curso crear(CursoRequestDTO requestDTO) {
        Curso curso = new Curso();
        curso.setNombre(requestDTO.getNombre());
        curso.setDescripcion(requestDTO.getDescripcion());
        return cursoRepository.save(curso);
    }

    public Curso crear(Curso curso) {
        return cursoRepository.save(curso);
    }

    public void eliminar(Long id) {
        cursoRepository.deleteById(id);
    }

    public Curso modificar(Long id, Curso cursoNuevo) {
        Optional<Curso> cursoExistente = cursoRepository.findById(id);
        if (cursoExistente.isPresent()) {
            Curso curso = cursoExistente.get();
            curso.setNombre(cursoNuevo.getNombre());
            curso.setDescripcion(cursoNuevo.getDescripcion());
            return cursoRepository.save(curso);
        }
        return null;
    }
}

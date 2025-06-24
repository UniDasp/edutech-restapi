package com.edutech.cl.main.service;

import com.edutech.cl.main.dto.request.PagoRequestDTO;
import com.edutech.cl.main.model.Pago;
import com.edutech.cl.main.model.Usuario;
import com.edutech.cl.main.model.Curso;
import com.edutech.cl.main.repository.PagoRepository;
import com.edutech.cl.main.repository.UsuarioRepository;
import com.edutech.cl.main.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    public PagoService(PagoRepository pagoRepository, UsuarioRepository usuarioRepository, CursoRepository cursoRepository) {
        this.pagoRepository = pagoRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
    }

    public List<Pago> listar() {
        return pagoRepository.findAll();
    }

    public Pago obtenerPorId(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + id));
    }

    public Pago crear(PagoRequestDTO requestDTO) {
        Usuario usuario = usuarioRepository.findById(requestDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + requestDTO.getUsuarioId()));

        Curso curso = cursoRepository.findById(requestDTO.getCursoId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con id: " + requestDTO.getCursoId()));

        Pago pago = new Pago();
        pago.setMonto(requestDTO.getMonto());
        pago.setFecha(requestDTO.getFecha());
        pago.setMetodo(requestDTO.getMetodo());
        pago.setEstado(requestDTO.getEstado());
        pago.setUsuario(usuario);
        pago.setCurso(curso);

        return pagoRepository.save(pago);
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

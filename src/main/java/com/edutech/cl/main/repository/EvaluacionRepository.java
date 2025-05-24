package com.edutech.cl.main.repository;

import com.edutech.cl.main.dto.response.EvaluacionDTO;
import com.edutech.cl.main.model.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {

    @Query("SELECT new com.edutech.cl.main.dto.response.EvaluacionDTO(e) FROM Evaluacion e WHERE e.curso.id = :cursoId")
    List<EvaluacionDTO> findByCursoId(@Param("cursoId") Long cursoId);
}

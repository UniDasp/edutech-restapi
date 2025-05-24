package com.edutech.cl.main.repository;

import com.edutech.cl.main.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}

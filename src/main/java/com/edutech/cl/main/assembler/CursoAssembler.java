package com.edutech.cl.main.assembler;

import com.edutech.cl.main.controller.CursoController;
import com.edutech.cl.main.model.Curso;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CursoAssembler implements RepresentationModelAssembler<Curso, EntityModel<Curso>> {

    @Override
    public EntityModel<Curso> toModel(Curso curso) {
        return EntityModel.of(curso,
                linkTo(methodOn(CursoController.class).obtenerPorId(curso.getId())).withSelfRel(),
                linkTo(methodOn(CursoController.class).listar()).withRel("cursos"));
    }

    @Override
    public CollectionModel<EntityModel<Curso>> toCollectionModel(Iterable<? extends Curso> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(CursoController.class).listar()).withSelfRel());
    }
} 

package com.edutech.cl.main.assembler;

import com.edutech.cl.main.controller.EvaluacionController;
import com.edutech.cl.main.model.Evaluacion;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EvaluacionAssembler implements RepresentationModelAssembler<Evaluacion, EntityModel<Evaluacion>> {

    @Override
    public EntityModel<Evaluacion> toModel(Evaluacion evaluacion) {
        return EntityModel.of(evaluacion,
                linkTo(methodOn(EvaluacionController.class).obtenerPorId(evaluacion.getId())).withSelfRel(),
                linkTo(methodOn(EvaluacionController.class).listar()).withRel("evaluaciones"));
    }

    @Override
    public CollectionModel<EntityModel<Evaluacion>> toCollectionModel(Iterable<? extends Evaluacion> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(EvaluacionController.class).listar()).withSelfRel());
    }
} 

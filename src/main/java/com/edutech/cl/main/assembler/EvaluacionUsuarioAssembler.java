package com.edutech.cl.main.assembler;

import com.edutech.cl.main.controller.EvaluacionUsuarioController;
import com.edutech.cl.main.model.EvaluacionUsuario;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EvaluacionUsuarioAssembler implements RepresentationModelAssembler<EvaluacionUsuario, EntityModel<EvaluacionUsuario>> {

    @Override
    public EntityModel<EvaluacionUsuario> toModel(EvaluacionUsuario evaluacionUsuario) {
        return EntityModel.of(evaluacionUsuario,
                linkTo(methodOn(EvaluacionUsuarioController.class).obtenerPorId(evaluacionUsuario.getId())).withSelfRel(),
                linkTo(methodOn(EvaluacionUsuarioController.class).listar()).withRel("evaluacionesUsuarios"));
    }

    @Override
    public CollectionModel<EntityModel<EvaluacionUsuario>> toCollectionModel(Iterable<? extends EvaluacionUsuario> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(EvaluacionUsuarioController.class).listar()).withSelfRel());
    }
} 

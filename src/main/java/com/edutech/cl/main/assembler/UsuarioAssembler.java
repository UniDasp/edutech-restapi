package com.edutech.cl.main.assembler;

import com.edutech.cl.main.controller.UsuarioController;
import com.edutech.cl.main.dto.response.UsuarioDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioAssembler implements RepresentationModelAssembler<UsuarioDTO, EntityModel<UsuarioDTO>> {

    @Override
    public EntityModel<UsuarioDTO> toModel(UsuarioDTO usuario) {
        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obtenerPorId(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listar()).withRel("usuarios"));
    }

    @Override
    public CollectionModel<EntityModel<UsuarioDTO>> toCollectionModel(Iterable<? extends UsuarioDTO> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(UsuarioController.class).listar()).withSelfRel());
    }
} 

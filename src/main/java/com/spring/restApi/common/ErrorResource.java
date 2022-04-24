package com.spring.restApi.common;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.spring.restApi.index.IndexController;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.validation.Errors;

public class ErrorResource extends EntityModel<Errors> {
    @JsonUnwrapped
    private final Errors errors;

    public ErrorResource(Errors errors) {
        this.errors = errors;
        linkTo(methodOn(IndexController.class).index()).withRel("index");
    }
}

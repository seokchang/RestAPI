package com.spring.restApi.index;

import com.spring.restApi.events.EventController;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/api")
    public RepresentationModel<?> index() {
        var index = new RepresentationModel<>();
        index.add(linkTo(EventController.class).withRel("events"));
        return index;
    }
}

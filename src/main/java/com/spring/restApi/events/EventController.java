package com.spring.restApi.events;

import com.spring.restApi.accounts.Account;
import com.spring.restApi.accounts.CurrentUser;
import com.spring.restApi.common.ErrorResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * produces : 출력하고자 하는 데이터 포맷 정의
 * consumes : 수신하고자 하는 데이터 포맷 정의
 */
@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable Integer id, @CurrentUser Account account) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return responseNotFound();
        }
        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource(event);
        if ((event.getAccount() != null) && (event.getAccount().equals(account))) {
            eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
        }
        return ResponseEntity.ok(eventResource);
    }

    @GetMapping
    public ResponseEntity<?> queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler, @CurrentUser Account account) {
        Page<Event> page = this.eventRepository.findAll(pageable);
        PagedModel<RepresentationModel<EventResource>> pageResources = assembler.toModel(page, EventResource::new);
        if (account != null) {
            pageResources.add(linkTo(EventController.class).withRel("create-event"));
        }
        return ResponseEntity.ok(pageResources);
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventDto eventDto, Errors errors, @CurrentUser Account account) {
        if (errors.hasErrors()) {
            return responseBadRequest(errors);
        }
        eventValidator.validate(eventDto, errors);

        if (errors.hasErrors()) {
            return responseBadRequest(errors);
        }
        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        event.setAccount(account);
        Event addEvent = this.eventRepository.save(event);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(addEvent.getId());
        URI createURI = selfLinkBuilder.toUri();

        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withSelfRel());
        eventResource.add(linkTo(EventController.class).withRel("update-event"));
        eventResource.add(linkTo(EventController.class).withRel("delete-event"));
        return ResponseEntity.created(createURI).body(addEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Integer id, @RequestBody @Valid EventDto eventDto, Errors errors, @CurrentUser Account account) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return responseNotFound();
        }

        if (errors.hasErrors()) {
            return responseBadRequest(errors);
        }

        this.eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return responseBadRequest(errors);
        }

        Event existingEvent = optionalEvent.get();
        if ((existingEvent.getAccount() != null) && (!existingEvent.getAccount().equals(account))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        this.modelMapper.map(eventDto, existingEvent);
        Event savedEvent = this.eventRepository.save(existingEvent);
        EventResource eventResource = new EventResource(savedEvent);
        return ResponseEntity.ok(eventResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Integer id) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return responseNotFound();
        }
        Event event = optionalEvent.get();
        this.eventRepository.delete(event);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<Event> responseNotFound() {
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<ErrorResource> responseBadRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorResource(errors));
    }
}

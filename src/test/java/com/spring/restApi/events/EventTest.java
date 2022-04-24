package com.spring.restApi.events;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class EventTest {
    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Spring REST API")
                .description("REST API development with Spring")
                .build();
        Assertions.assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        String name = "Event";
        String description = "Spring";

        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        Assertions.assertThat(event.getName()).isEqualTo("Event");
        Assertions.assertThat(event.getDescription()).isEqualTo("Spring");
    }
}

package com.spring.restApi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {
    // EventDto 유효성 검사
    public void validate(EventDto eventDto, Errors errors) {
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0) {
            // Field Error
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong");
            errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong");
            // Global Error
            errors.reject("wrongPrice", "Values for prices are wrong");
        }
        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
                endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
                endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("endEventDateTime", "wrongValue", "EndEventDateTime is wrong");
        }
    }
}

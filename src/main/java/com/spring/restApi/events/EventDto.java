package com.spring.restApi.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime beginEnrollmentDateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime closeEnrollmentDateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime beginEventDateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endEventDateTime;
    private String location;
    @Min(0)
    private int basePrice;
    private int maxPrice;
    private int limitOfEnrollment;
}

package com.trs.tickets.model.dto;

import com.trs.tickets.model.entity.Place;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SessionDto extends BaseDto {
    private Long movieId;
    private Long hallId;

    private LocalDateTime sessionDateTime;

    private List<Place> places;
}
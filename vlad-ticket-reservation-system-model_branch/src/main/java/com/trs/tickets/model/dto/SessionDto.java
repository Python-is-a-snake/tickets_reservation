package com.trs.tickets.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

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
}
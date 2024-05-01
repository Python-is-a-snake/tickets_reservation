package com.trs.tickets.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RatingDto extends BaseDto {
    private Long userId;
    private Long movieId;
    private Short score;
}

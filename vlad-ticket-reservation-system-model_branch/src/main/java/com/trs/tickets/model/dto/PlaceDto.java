package com.trs.tickets.model.dto;

import com.trs.tickets.model.PlaceType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PlaceDto extends BaseDto {
    private Long      hallId;
    private Integer   row;
    private Integer   number;
    private PlaceType placeType;
}

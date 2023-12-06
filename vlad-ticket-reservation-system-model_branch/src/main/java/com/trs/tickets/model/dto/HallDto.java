package com.trs.tickets.model.dto;

import com.trs.tickets.model.HallType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class HallDto extends BaseDto {

    private Long cinemaId;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private HallType type;

//    private List<PlaceDto> places = new ArrayList<>();
}

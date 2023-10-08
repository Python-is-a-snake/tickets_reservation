package com.trs.tickets.model.dto;

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
public class CinemaDto extends BaseDto {
    private String name;
    private String address;
    private String phone;
    private String email;
    private List<HallDto> halls = new ArrayList<>();
}

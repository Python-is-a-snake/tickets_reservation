package com.trs.tickets.mappers;

import com.trs.tickets.model.dto.CinemaDto;
import com.trs.tickets.model.entity.Cinema;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {HallMapper.class})
public interface CinemaMapper extends GenericMapper<Cinema, CinemaDto> {}

package com.trs.tickets.mappers;

import com.trs.tickets.model.dto.HallDto;
import com.trs.tickets.model.entity.Cinema;
import com.trs.tickets.model.entity.Hall;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PlaceMapper.class})
public interface HallMapper extends GenericMapper<Hall, HallDto> {
    @Override
    @Mapping(target = "cinema", ignore = true)
    Hall convert(HallDto dto);

    @Override
    @Mapping(target = "cinemaId", source = "cinema")
    HallDto convert(Hall entity);

    default Long map(Cinema cinema) {
        return cinema.getId();
    }
}

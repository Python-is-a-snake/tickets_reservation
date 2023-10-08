package com.trs.tickets.mappers;

import com.trs.tickets.model.dto.PlaceDto;
import com.trs.tickets.model.entity.Hall;
import com.trs.tickets.model.entity.Place;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaceMapper extends GenericMapper<Place, PlaceDto> {
    @Override
    @Mapping(target = "hall", ignore = true)
    Place convert(PlaceDto dto);

    @Override
    @Mapping(target = "hallId", source = "hall")
    PlaceDto convert(Place entity);

    default Long map(Hall hall) {
        return hall.getId();
    }
}

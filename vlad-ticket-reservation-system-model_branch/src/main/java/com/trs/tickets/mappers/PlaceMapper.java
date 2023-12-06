package com.trs.tickets.mappers;

import com.trs.tickets.model.dto.PlaceDto;
import com.trs.tickets.model.entity.Hall;
import com.trs.tickets.model.entity.Place;
import com.trs.tickets.model.entity.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaceMapper extends GenericMapper<Place, PlaceDto> {
//    @Override
//    @Mapping(target = "hall", ignore = true)
//    Place convert(PlaceDto dto);

    @Override
    @Mapping(target = "session", ignore = true)
    Place convert(PlaceDto dto);
//    @Override
//    @Mapping(target = "hallId", source = "hall")
//    PlaceDto convert(Place entity);

//    @Override
//    @Mapping(target = "hallId", source = "hall")
//    PlaceDto convert(Place entity);

    @Override
    @Mapping(target = "sessionId", source = "session")
    PlaceDto convert(Place entity);

    default Long map(Hall hall) {
        return hall.getId();
    }
    default Long map(Session session) {
        return session.getId();
    }
}

package com.trs.tickets.mappers;

import com.trs.tickets.model.dto.SessionDto;
import com.trs.tickets.model.entity.Cinema;
import com.trs.tickets.model.entity.Hall;
import com.trs.tickets.model.entity.Movie;
import com.trs.tickets.model.entity.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MovieMapper.class})
public interface SessionMapper extends GenericMapper<Session, SessionDto> {

    @Override
    @Mapping(target = "hallId", source = "hall")
    @Mapping(target = "movieId", source = "movie")
    SessionDto convert(Session entity);

    default Long map(Movie movie) {
        return movie.getId();
    }

    default Long map(Hall hall) {
        return hall.getId();
    }
}

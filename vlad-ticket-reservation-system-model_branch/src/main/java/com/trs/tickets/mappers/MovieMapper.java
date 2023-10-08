package com.trs.tickets.mappers;

import com.trs.tickets.model.dto.MovieDto;
import com.trs.tickets.model.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface MovieMapper extends GenericMapper<Movie, MovieDto> {}

package com.trs.tickets.mappers;

import com.trs.tickets.model.HallType;
import com.trs.tickets.model.dto.CinemaDto;
import com.trs.tickets.model.dto.HallDto;
import com.trs.tickets.model.dto.MovieDto;
import com.trs.tickets.model.entity.Cinema;
import com.trs.tickets.model.entity.Hall;
import com.trs.tickets.model.entity.Movie;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MovieMapperTest extends AbstractMapperTest {
    @Autowired
    private MovieMapper mapper;

    @Nested
    class EntityToDtoTest {
        @Test
        void convertEntityToDto() {
            // given
            Movie movie = new Movie();
            movie.setId(1L);
            movie.setTitle("movie title");

            // when
            MovieDto movieDto = mapper.convert(movie);

            // then
            assertEquals(movie.getId(), movieDto.getId());
            assertEquals(movie.getTitle(), movieDto.getTitle());
        }
    }

    @Nested
    class DtoToEntityTest {
        @Test
        void convertDtoToEntity() {
            // given
            MovieDto movieDto = new MovieDto();
            movieDto.setId(1L);

            // when
            Movie movie = mapper.convert(movieDto);

            // then
            assertEquals(movieDto.getId(), movie.getId());
        }
    }
}

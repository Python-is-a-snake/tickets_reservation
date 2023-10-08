package com.trs.tickets.mappers;

import com.trs.tickets.model.HallType;
import com.trs.tickets.model.dto.CinemaDto;
import com.trs.tickets.model.dto.HallDto;
import com.trs.tickets.model.entity.Cinema;
import com.trs.tickets.model.entity.Hall;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CinemaMapperTest extends AbstractMapperTest {
    @Autowired
    private CinemaMapper mapper;

    @Nested
    class EntityToDtoTest {
        @Test
        void convertEntityToDto() {
            // given
            Cinema cinema = new Cinema();
            cinema.setId(1L);
            cinema.setName("cinema name");
            cinema.setEmail("cinema@email.com");
            cinema.setPhone("0123456789");
            cinema.setAddress("cinema address");

            // when
            CinemaDto cinemaDto = mapper.convert(cinema);

            // then
            assertEquals(cinema.getId(), cinemaDto.getId());
            assertEquals(cinema.getName(), cinemaDto.getName());
            assertEquals(cinema.getEmail(), cinemaDto.getEmail());
            assertEquals(cinema.getPhone(), cinemaDto.getPhone());
            assertEquals(cinema.getAddress(), cinemaDto.getAddress());
        }

        @Test
        void convertEntityToDtoWithNestedEntityList() {
            // given
            Cinema cinema = new Cinema();
            cinema.setId(1L);
            cinema.setName("cinema name");
            cinema.setEmail("cinema@email.com");
            cinema.setPhone("0123456789");
            cinema.setAddress("cinema address");

            Hall hall = new Hall();
            hall.setId(2L);
            hall.setName("hall name");
            hall.setType(HallType.HALL_2D);
            hall.setCinema(cinema);

            cinema.setHalls(List.of(hall));

            // when
            CinemaDto cinemaDto = mapper.convert(cinema);

            // then
            assertEquals(cinema.getId(), cinemaDto.getId());
            assertEquals(cinema.getName(), cinemaDto.getName());
            assertEquals(cinema.getEmail(), cinemaDto.getEmail());
            assertEquals(cinema.getPhone(), cinemaDto.getPhone());
            assertEquals(cinema.getAddress(), cinemaDto.getAddress());

            assertEquals(cinema.getHalls().size(), cinemaDto.getHalls().size());
            assertEquals(cinema.getHalls().get(0).getId(), cinemaDto.getHalls().get(0).getId());
            assertEquals(cinema.getHalls().get(0).getType(), cinemaDto.getHalls().get(0).getType());
            assertEquals(cinema.getHalls().get(0).getName(), cinemaDto.getHalls().get(0).getName());
            assertEquals(cinema.getHalls().get(0).getCinema().getId(), cinemaDto.getHalls().get(0).getCinemaId());
        }
    }

    @Nested
    class DtoToEntityTest {
        @Test
        void convertDtoToEntity() {
            // given
            CinemaDto cinemaDto = new CinemaDto();
            cinemaDto.setId(1L);
            cinemaDto.setName("cinema name");
            cinemaDto.setEmail("cinema@email.com");
            cinemaDto.setPhone("0123456789");
            cinemaDto.setAddress("cinema address");

            // when
            Cinema cinema = mapper.convert(cinemaDto);

            // then
            assertEquals(cinemaDto.getId(), cinema.getId());
            assertEquals(cinemaDto.getName(), cinema.getName());
            assertEquals(cinemaDto.getEmail(), cinema.getEmail());
            assertEquals(cinemaDto.getPhone(), cinema.getPhone());
            assertEquals(cinemaDto.getAddress(), cinema.getAddress());
        }

        @Test
        void convertDtoToEntityWithNestedEntityList() {
            // given
            CinemaDto cinemaDto = new CinemaDto();
            cinemaDto.setId(1L);
            cinemaDto.setName("cinema name");
            cinemaDto.setEmail("cinema@email.com");
            cinemaDto.setPhone("0123456789");
            cinemaDto.setAddress("cinema address");

            HallDto hallDto = new HallDto();
            hallDto.setId(2L);
            hallDto.setName("New Hall");
            hallDto.setType(HallType.HALL_2D);
            hallDto.setCinemaId(cinemaDto.getId());

            cinemaDto.setHalls(List.of(hallDto));

            // when
            Cinema cinema = mapper.convert(cinemaDto);

            // then
            assertEquals(cinemaDto.getId(), cinema.getId());
            assertEquals(cinemaDto.getName(), cinema.getName());
            assertEquals(cinemaDto.getEmail(), cinema.getEmail());
            assertEquals(cinemaDto.getPhone(), cinema.getPhone());
            assertEquals(cinemaDto.getAddress(), cinema.getAddress());

            assertEquals(cinemaDto.getHalls().size(), cinema.getHalls().size());
            assertEquals(cinemaDto.getHalls().get(0).getName(), cinema.getHalls().get(0).getName());
            assertEquals(cinemaDto.getHalls().get(0).getType(), cinema.getHalls().get(0).getType());
            assertEquals(cinemaDto.getHalls().get(0).getId(), cinema.getHalls().get(0).getId());
            assertNull(cinema.getHalls().get(0).getCinema());
        }
    }
}

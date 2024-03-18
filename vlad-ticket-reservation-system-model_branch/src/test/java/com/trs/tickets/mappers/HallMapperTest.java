//package com.trs.tickets.mappers;
//
//import com.trs.tickets.model.HallType;
//import com.trs.tickets.model.PlaceType;
//import com.trs.tickets.model.dto.CinemaDto;
//import com.trs.tickets.model.dto.HallDto;
//import com.trs.tickets.model.entity.Cinema;
//import com.trs.tickets.model.entity.Hall;
//import com.trs.tickets.model.entity.Place;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//class HallMapperTest extends AbstractMapperTest {
//    @Autowired
//    private HallMapper mapper;
//
//    @Nested
//    class EntityToDtoTest {
//        @Test
//        void convertEntityToDto() {
//            // given
//            Cinema cinema = new Cinema();
//            cinema.setId(1L);
//            cinema.setName("cinema name");
//            cinema.setEmail("cinema@email.com");
//            cinema.setPhone("0123456789");
//            cinema.setAddress("cinema address");
//
//            Hall hall = new Hall();
//            hall.setId(2L);
//            hall.setName("hall name");
//            hall.setType(HallType.HALL_2D);
//            hall.setCinema(cinema);
//
//            // when
//            HallDto hallDto = mapper.convert(hall);
//
//            // then
//            assertEquals(hall.getId(), hallDto.getId());
//            assertEquals(hall.getName(), hallDto.getName());
//            assertEquals(hall.getType(), hallDto.getType());
//            assertEquals(hall.getCinema().getId(), hallDto.getCinemaId());
////            assertEquals(hall.getPlaces().size(), hallDto.getPlaces().size());
//        }
//
//        @Test
//        void convertEntityToDtoWithNestedEntityList() {
//            // given
//            Cinema cinema = new Cinema();
//            cinema.setId(1L);
//            cinema.setName("cinema name");
//            cinema.setEmail("cinema@email.com");
//            cinema.setPhone("0123456789");
//            cinema.setAddress("cinema address");
//
//            Hall hall = new Hall();
//            hall.setId(2L);
//            hall.setName("hall name");
//            hall.setType(HallType.HALL_2D);
//            hall.setCinema(cinema);
//
//            Place place = new Place();
//            place.setId(3L);
//            place.setRow(1);
//            place.setNumber(2);
//            place.setPlaceType(PlaceType.NORMAL);
//            place.setHall(hall);
//
//            hall.setPlaces(List.of(place));
//            cinema.setHalls(List.of(hall));
//
//            // when
//            HallDto hallDto = mapper.convert(hall);
//
//            // then
//            assertEquals(hall.getId(), hallDto.getId());
//            assertEquals(hall.getName(), hallDto.getName());
//            assertEquals(hall.getType(), hallDto.getType());
//            assertEquals(hall.getCinema().getId(), hallDto.getCinemaId());
//            assertEquals(hall.getPlaces().size(), hallDto.getPlaces().size());
//            assertEquals(hall.getPlaces().get(0).getId(), hallDto.getPlaces().get(0).getId());
//            assertEquals(hall.getPlaces().get(0).getRow(), hallDto.getPlaces().get(0).getRow());
//            assertEquals(hall.getPlaces().get(0).getNumber(), hallDto.getPlaces().get(0).getNumber());
//            assertEquals(hall.getPlaces().get(0).getPlaceType(), hallDto.getPlaces().get(0).getPlaceType());
//            assertEquals(hall.getPlaces().get(0).getHall().getId(), hallDto.getPlaces().get(0).getHallId());
//        }
//    }
//
//    @Nested
//    class DtoToEntityTest {
//        @Test
//        void convertDtoToEntity() {
//            // given
//            CinemaDto cinemaDto = new CinemaDto();
//            cinemaDto.setId(1L);
//            cinemaDto.setName("cinema name");
//            cinemaDto.setEmail("cinema@email.com");
//            cinemaDto.setPhone("0123456789");
//            cinemaDto.setAddress("cinema address");
//
//            HallDto hallDto = new HallDto();
//            hallDto.setId(2L);
//            hallDto.setName("hall name");
//            hallDto.setType(HallType.HALL_2D);
//            hallDto.setCinemaId(cinemaDto.getId());
//
//            // when
//            Hall hall = mapper.convert(hallDto);
//
//            // then
//            assertEquals(hallDto.getId(), hall.getId());
//            assertEquals(hallDto.getName(), hall.getName());
//            assertEquals(hallDto.getType(), hall.getType());
//            assertNull(hall.getCinema());
//        }
//
//    //    @Test
//    //    void convertDtoToEntityNested() {
//    //        // given
//    //        CinemaDto cinemaDto = new CinemaDto();
//    //        cinemaDto.setId(1L);
//    //        cinemaDto.setName("cinema name");
//    //        cinemaDto.setEmail("cinema@email.com");
//    //        cinemaDto.setPhone("0123456789");
//    //        cinemaDto.setAddress("cinema address");
//    //
//    //        HallDto hallDto = new HallDto();
//    //        hallDto.setId(2L);
//    //        hallDto.setName("New Hall");
//    //        hallDto.setType(HallType.HALL_2D);
//    //        hallDto.setCinemaId(cinemaDto.getId());
//    //
//    //        cinemaDto.setHalls(List.of(hallDto));
//    //
//    //        // when
//    //        Cinema cinema = mapper.convert(cinemaDto);
//    //
//    //        // then
//    //        assertEquals(cinemaDto.getId(), cinema.getId());
//    //        assertEquals(cinemaDto.getName(), cinema.getName());
//    //        assertEquals(cinemaDto.getEmail(), cinema.getEmail());
//    //        assertEquals(cinemaDto.getPhone(), cinema.getPhone());
//    //        assertEquals(cinemaDto.getAddress(), cinema.getAddress());
//    //
//    //        assertEquals(cinemaDto.getHalls().size(), cinema.getHalls().size());
//    //        assertEquals(cinemaDto.getHalls().get(0).getName(), cinema.getHalls().get(0).getName());
//    //        assertEquals(cinemaDto.getHalls().get(0).getType(), cinema.getHalls().get(0).getType());
//    //        assertEquals(cinemaDto.getHalls().get(0).getId(), cinema.getHalls().get(0).getId());
//    //        assertNull(cinema.getHalls().get(0).getCinema());
//    //    }
//
//    }
//}

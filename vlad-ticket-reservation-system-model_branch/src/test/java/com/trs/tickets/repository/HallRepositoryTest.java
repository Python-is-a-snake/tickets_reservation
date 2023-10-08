package com.trs.tickets.repository;

import com.trs.tickets.model.HallType;
import com.trs.tickets.model.entity.Cinema;
import com.trs.tickets.model.entity.Hall;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HallRepositoryTest {
    @Autowired
    HallRepository hallRepository;

    @Autowired
    CinemaRepository cinemaRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Test
    void saveHallShouldWork(){
        Cinema cinema = new Cinema();
        cinema.setName("cinema");
        cinema.setAddress("address");
        cinema.setPhone("123457678");
        cinema.setEmail("cinema@kino.ua");
        cinemaRepository.save(cinema);

        Hall hall = new Hall();
        hall.setName("hallNo1");
        hall.setType(HallType.HALL_2D);
        hall.setCinema(cinema);
        hallRepository.save(hall);

        cinema.getHalls().add(hall);
        cinemaRepository.save(cinema);

        cinemaRepository.flush();
        hallRepository.flush();

        Cinema cinema1 = cinemaRepository.findById(cinema.getId()).get();
        List<Hall> halls1 = cinema1.getHalls();

        // TODO add proper assertions to saved Hall
        assertNotNull(hall.getId());
    }
}

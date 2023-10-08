package com.trs.tickets.repository;

import com.trs.tickets.model.entity.Cinema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CinemaRepositoryTest {
    @Autowired
    private CinemaRepository cinemaRepository;
    private Cinema cinema;

    @BeforeEach
    void setUp() {
        this.cinema = new Cinema();
    }

    @Test
    void saveCinemaShouldWork() {
        cinema.setName("Planeta-Kino");
        cinema.setAddress("Kyiv, Ukraine");
        cinema.setEmail("kyiv@planeta-kino.ua");
        cinema.setPhone("093123345"); //WHY IT WORKED WITHOUT PHONE

        Cinema saved = cinemaRepository.saveAndFlush(cinema);
        assertNotNull(saved.getId());
    }

    @Test
    void saveCinemaWithoutNameShouldFail() {
        //cinema.setName("Planeta-Kino");
        cinema.setAddress("Kyiv, Ukraine");
        cinema.setEmail("kyiv@planeta-kino.ua");
        cinema.setPhone("093123345");

        assertThrows(DataIntegrityViolationException.class, () -> cinemaRepository.save(cinema));
    }

    @Test
    void saveCinemaWithoutAddressShouldFail() {
        cinema.setName("Planeta-Kino");
        //cinema.setAddress("Kyiv, Ukraine");
        cinema.setEmail("kyiv@planeta-kino.ua");
        cinema.setPhone("093123345");

        assertThrows(DataIntegrityViolationException.class, () -> cinemaRepository.save(cinema));
    }

    @Test
    void saveCinemaWithoutEmailShouldFail() {
        cinema.setName("Planeta-Kino");
        cinema.setAddress("Kyiv, Ukraine");
        //cinema.setEmail("kyiv@planeta-kino.ua");
        cinema.setPhone("093123345");

        assertThrows(DataIntegrityViolationException.class, () -> cinemaRepository.save(cinema));
    }
}

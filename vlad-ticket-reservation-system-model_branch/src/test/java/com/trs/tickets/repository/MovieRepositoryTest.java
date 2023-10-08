package com.trs.tickets.repository;

import com.trs.tickets.model.entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class MovieRepositoryTest {
    @Autowired
    private MovieRepository movieRepository;

    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie();
    }

    @Test
    void saveMovieShouldWork() {
        movie.setCountry("USA");
        movie.setTitle("American Beans");
        movie.setDirector("John Mock");

        assertNotNull(movieRepository.save(movie).getId());
    }

    @Test
    void saveMovieWithoutTitleShouldFail() {
        movie.setDirector("John Mock");
        movie.setActors("Minion 1, Minion 2");

        assertThrows(DataIntegrityViolationException.class, () -> movieRepository.saveAndFlush(movie));
    }
}

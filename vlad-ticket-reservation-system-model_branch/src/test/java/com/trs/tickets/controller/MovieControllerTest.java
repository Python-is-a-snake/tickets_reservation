package com.trs.tickets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trs.tickets.model.dto.MovieDto;
import com.trs.tickets.model.entity.Movie;
import com.trs.tickets.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.BDDMockito.given;

@WebMvcTest(MovieController.class)
class MovieControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<Long> idCaptor;

    @Captor
    ArgumentCaptor<MovieDto> movieCaptor;

    @Captor
    ArgumentCaptor<List<MovieDto>> moviesListCaptor;

    private MovieDto testMovie;

    private final int TEST_ID = 1;

    @BeforeEach
    public void setUp() {
        testMovie = new MovieDto();
        testMovie.setId(1L);
        testMovie.setTitle("Test Movie");
        testMovie.setDescription("This is a test movie");
        testMovie.setGenre("Comedy");
    }

    @Test
    void getAllMovies_returnsListOfMovies() throws Exception {
        Movie movie2 = new Movie();
        movie2.setTitle("Movie 2");
        ArrayList<MovieDto> testArray = new ArrayList<>(Arrays.asList(testMovie, new MovieDto()));
        given(movieService.getAllMovies())
                .willReturn(testArray);

        mockMvc.perform(get("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)));
    }

    //@Test
    void getMovieById_returnsMovie() throws Exception {
        //given(movieService.getMovieById(anyLong())).willReturn(testMovie);


        mockMvc.perform(get("/api/movies/" + TEST_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("Test Movie")))
                .andExpect(jsonPath("$.description", is("This is a test movie")))
                .andExpect(jsonPath("$.genre", is("Comedy")));
    }

    @Test
    void testAddMovie_returnsAddedMovie() throws Exception {
        testMovie.setTitle("New Title");

        given(movieService.createMovie(any(MovieDto.class))).willReturn(testMovie);

        mockMvc.perform(post("/api/movies")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMovie)))
                .andExpect(jsonPath("$.title", is(testMovie.getTitle())));

        verify(movieService).createMovie(movieCaptor.capture());
        assertEquals(testMovie, movieCaptor.getValue());
    }

    @Test
    void testAddMovies_returnsListOfAddedMovies() throws Exception {
        Movie movie2 = new Movie();
        movie2.setTitle("Movie 2");
        ArrayList<MovieDto> testArray = new ArrayList<>(Arrays.asList(testMovie, new MovieDto()));

        mockMvc.perform(post("/api/movies/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testArray)))
                .andExpect(jsonPath("$.length()", is(2)));

        verify(movieService).addMovies(moviesListCaptor.capture());
    }

    @Test
    void testUpdateMovie_returnsUpdatedMovie() throws Exception {
        testMovie.setId(1L);

        mockMvc.perform(put("/api/movies") // Should be "/api/movies/{id}"?
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMovie)));

        verify(movieService).updateMovie(movieCaptor.capture());
        assertEquals(testMovie, movieCaptor.getValue());

    }

    @Test
    void testDeleteMovie() throws Exception {
        mockMvc.perform(delete("/api/movies/" + TEST_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        verify(movieService).deleteMovie(idCaptor.capture());
        assertEquals(TEST_ID, idCaptor.getValue());
    }

}

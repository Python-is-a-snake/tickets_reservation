package com.trs.tickets.service;

import com.trs.tickets.mappers.MovieMapper;
import com.trs.tickets.model.dto.MovieDto;
import com.trs.tickets.model.dto.projection.MovieMostSessionsProjection;
import com.trs.tickets.model.entity.Movie;
import com.trs.tickets.repository.MovieRepository;
import com.trs.tickets.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final SessionRepository sessionRepository;
    private final SessionService sessionService;
    private final MovieMapper     movieMapper;

    public Page<MovieDto> getAllMovies(Integer page, Integer size) {
        log.info("Finding all Movies");
        Pageable pageable = PageRequest.of(page, size);
        return movieRepository.findAll(pageable).map(movieMapper::convert);
    }

    public Page<MovieDto> getActiveMovies(Integer page, Integer size) {
        log.info("Finding active Movies - Paged");
        Pageable pageable = PageRequest.of(page, size);
        return movieRepository.findAllByIsActiveTrue(pageable).map(movieMapper::convert);
    }

    public List<MovieDto> getActiveMovies() {
        log.info("Finding active Movies");
        return movieRepository.findAllByIsActiveTrue().stream().map(movieMapper::convert).toList();
    }

    public List<MovieDto> getAllMovies() {
        log.info("Finding all Movies");
        return movieRepository.findAll().stream().map(movieMapper::convert).toList();
    }

    public Page<MovieDto> getMoviesByTitle(String title, Integer page, Integer size) {
        log.info("Finding Movie by title: " + title);
        Pageable pageable = PageRequest.of(page, size);
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable).map(movieMapper::convert);
    }

    public Page<MovieDto> getActiveMoviesByTitle(String title, Integer page, Integer size) {
        log.info("Finding Active Movies by title: " + title);
        Pageable pageable = PageRequest.of(page, size);
        return movieRepository.findByTitleContainingIgnoreCaseAndIsActiveTrue(title, pageable).map(movieMapper::convert);
    }

    public MovieDto getMovieById(Long id) {
        log.info("Finding Movie with ID: " + id);
        Optional<Movie> movieOpt = movieRepository.findById(id);
        return movieOpt.map(movieMapper::convert).orElse(null);
    }

    public MovieDto createMovie(MovieDto movieDTO) {
        log.info("Creating new Movie: " + movieDTO);
        var entity = movieRepository.save(movieMapper.convert(movieDTO));
        return movieMapper.convert(entity);
    }

    public MovieDto updateMovie(MovieDto movieDTO) {
        log.info("Updating Movie: " + movieDTO);
        var entity = movieRepository.save(movieMapper.convert(movieDTO));
        return movieMapper.convert(entity);
    }

    public void deleteMovie(Long id) {
        log.info("Deleting Sessions by Movie ID: " + id);
        sessionRepository.findByMovieId(id).forEach(session -> sessionService.deleteSession(session.getId()));
        log.info("Deleting Movie by ID: " + id);
        movieRepository.deleteById(id);
    }

    public void deactivateMovie(Long id) {
        log.info("Deactivating Sessions by Movie ID: " + id);
//        sessionRepository.findByMovieId(id).forEach(session -> sessionService.inactiveSession(session.getId()));
        log.info("Finding Movie by ID: " + id);
        Optional<Movie> movieOpt = movieRepository.findById(id);
        Movie movie = movieOpt.orElseThrow(() -> new NoSuchElementException("Unable to find movie with ID " + id));
        log.info("Deactivating Movie by ID: " + id);
        movie.setIsActive(false);
        log.info("Saving Movie by ID: " + id);
        movieRepository.save(movie);
    }

    public List<MovieDto> addMovies(List<MovieDto> movies) {
        List<Movie> entities = movies.stream().map(movieMapper::convert).toList();
        entities = movieRepository.saveAll(entities);
        return entities.stream().map(movieMapper::convert).toList();
    }

    public List<MovieMostSessionsProjection> getMoviesWithMostSessions() {
        log.info("Searching 10 movies with most sessions");
        List<MovieMostSessionsProjection> moviesWithMostSessions = movieRepository.findMoviesWithMostSessions();
        log.info("Found {} values", moviesWithMostSessions.size());
        moviesWithMostSessions.forEach(m -> System.out.println(m.getTitle() + " " + m.getSessionCount()));
        return moviesWithMostSessions;
    }
}

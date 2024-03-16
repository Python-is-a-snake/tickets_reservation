package com.trs.tickets.service;

import com.trs.tickets.mappers.MovieMapper;
import com.trs.tickets.model.dto.MovieDto;
import com.trs.tickets.model.entity.Movie;
import com.trs.tickets.repository.MovieRepository;
import com.trs.tickets.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final SessionRepository sessionRepository;
    private final SessionService sessionService;
    private final MovieMapper     movieMapper;

    public List<MovieDto> getMoviesByTitle(String title) {
        log.info("Finding Movie by title: " + title);
        return movieRepository.findByTitleContainingIgnoreCase(title).stream().map(movieMapper::convert).toList();
    }

    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll().stream().map(movieMapper::convert).toList();
    }

    public MovieDto getMovieById(Long id) {
        Optional<Movie> movieOpt = movieRepository.findById(id);
        return movieOpt.map(movieMapper::convert).orElse(null);
    }

    public MovieDto addMovie(MovieDto movieDTO) {
        var entity = movieRepository.save(movieMapper.convert(movieDTO));
        return movieMapper.convert(entity);
    }

    public MovieDto updateMovie(MovieDto movieDTO) {
        var entity = movieRepository.save(movieMapper.convert(movieDTO));
        return movieMapper.convert(entity);
    }

    public void deleteMovie(Long id) {
        sessionRepository.findByMovieId(id).forEach(session -> sessionService.deleteSession(session.getId()));
        movieRepository.deleteById(id);
    }

    public List<MovieDto> addMovies(List<MovieDto> movies) {
        List<Movie> entities = movies.stream().map(movieMapper::convert).toList();
        entities = movieRepository.saveAll(entities);
        return entities.stream().map(movieMapper::convert).toList();
    }

    public Page<Movie> getMoviesPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        return movieRepository.findAll(pageable);
    }

}

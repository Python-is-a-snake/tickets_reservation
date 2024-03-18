package com.trs.tickets.service;

import com.trs.tickets.mappers.MovieMapper;
import com.trs.tickets.model.dto.MovieDto;
import com.trs.tickets.model.entity.Movie;
import com.trs.tickets.repository.MovieRepository;
import com.trs.tickets.repository.SessionRepository;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public Page<MovieDto> getMovies(Integer page, Integer size) {


        Pageable pageable = PageRequest.of(page, size);

        return movieRepository.findAll(pageable).map(movieMapper::convert);
    }

    public Page<MovieDto> getMoviesByTitle(String title, Integer page, Integer size) {
        log.info("Finding Movie by title: " + title);

        Pageable pageable = PageRequest.of(page, size);

        return movieRepository.findByTitleContainingIgnoreCase(title, pageable).map(movieMapper::convert);
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


}

package com.trs.tickets.service;

import com.trs.tickets.model.entity.Cinema;
import com.trs.tickets.repository.CinemaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CinemaService {
    private final CinemaRepository cinemaRepository;

    public List<Cinema> getAllCinemas() {
        log.info("Getting all cinemas");
        return cinemaRepository.findAll();
    }

    public Cinema getCinemaById(Long id) {
        log.info("Finding Cinema with id: " + id);
        return cinemaRepository.findById(id).orElse(null);
    }

    public Cinema addCinema(Cinema cinema) {
        log.info("Adding new Cinema");
        return cinemaRepository.save(cinema);
    }

    public Cinema updateCinema(Cinema cinema) {
        return cinemaRepository.save(cinema);
    }

    public void deleteCinema(Long id) {
        cinemaRepository.deleteById(id);
    }
}

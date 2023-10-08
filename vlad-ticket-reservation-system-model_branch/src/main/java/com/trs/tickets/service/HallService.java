package com.trs.tickets.service;

import com.trs.tickets.model.entity.Cinema;
import com.trs.tickets.model.entity.Hall;
import com.trs.tickets.repository.HallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HallService {
    private final HallRepository hallRepository;
    private final CinemaService  cinemaService;

    public List<Hall> getAllHalls() {
        return hallRepository.findAll();
    }

    public Hall getHallById(Long id) {
        return hallRepository.findById(id).orElse(null);
    }

    public Hall addHall(Hall hall) {
        Cinema cinema = cinemaService.getCinemaById(hall.getCinema().getId());
        hall.setCinema(cinema);
        return hallRepository.save(hall);
    }

    public Hall updateHall(Hall hall) {
        return hallRepository.save(hall);
    }

    public void deleteHall(Long id) {
        hallRepository.deleteById(id);
    }
}

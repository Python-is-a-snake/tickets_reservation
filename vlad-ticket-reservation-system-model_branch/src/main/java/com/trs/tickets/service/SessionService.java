package com.trs.tickets.service;

import com.trs.tickets.mappers.SessionMapper;
import com.trs.tickets.model.dto.SessionDto;
import com.trs.tickets.model.entity.Hall;
import com.trs.tickets.model.entity.Movie;
import com.trs.tickets.model.entity.Place;
import com.trs.tickets.model.entity.Session;
import com.trs.tickets.repository.HallRepository;
import com.trs.tickets.repository.MovieRepository;
import com.trs.tickets.repository.SessionRepository;
import com.trs.tickets.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {
    private final SessionRepository sessionRepository;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;
    private final TicketRepository ticketRepository;
    private final SessionMapper sessionMapper;
    private final TicketService ticketService;
    private final PlaceService placeService;

    public Page<Session> getAllSessions(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        return sessionRepository.findAll(pageable);
    }

    public SessionDto getSessionById(Long id) {
        Optional<Session> session = sessionRepository.findById(id);
        if(session.isEmpty()){
            return null;
        }
        else return sessionMapper.convert(session.get());
    }

    public Session addSession(Session session) {
        return sessionRepository.save(session);
    }

    public Session createSession(Long movieId, Long hallId, SessionDto sessionDto) {
        Movie movie = movieRepository.findById(movieId).get();
        Hall hall = hallRepository.findById(hallId).get();
        Session session = sessionMapper.convert(sessionDto);

        session.setMovie(movie);
        session.setHall(hall);

        sessionRepository.save(session);

        List<Place> places = placeService.initPlaces(session, hall.getType());
        session.setPlaces(places);

        return sessionRepository.save(session);
    }

    public SessionDto updateSession(SessionDto sessionDto) {
        var entity = sessionRepository.save(sessionMapper.convert(sessionDto));
        return sessionMapper.convert(entity);
    }

    public void deleteSession(Long sessionId) {
        ticketRepository.deleteTicketsBySessionId(sessionId);
        ticketRepository.deletePlacesBySessionId(sessionId);

        sessionRepository.deleteById(sessionId);
    }


}

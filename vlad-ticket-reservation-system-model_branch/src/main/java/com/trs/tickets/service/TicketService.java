package com.trs.tickets.service;

import com.trs.tickets.mappers.TicketMapper;
import com.trs.tickets.model.dto.PlaceDto;
import com.trs.tickets.model.dto.SessionDto;
import com.trs.tickets.model.dto.TicketDto;
import com.trs.tickets.model.dto.UserDto;
import com.trs.tickets.model.entity.Place;
import com.trs.tickets.model.entity.Session;
import com.trs.tickets.model.entity.Ticket;
import com.trs.tickets.model.entity.User;
import com.trs.tickets.repository.PlaceRepository;
import com.trs.tickets.repository.SessionRepository;
import com.trs.tickets.repository.TicketRepository;
import com.trs.tickets.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final PlaceRepository placeRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final TicketMapper ticketMapper;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public Ticket addTicket(TicketDto ticketDto, Long placeId, Long sessionId, Long userId) {
        Place place = placeRepository.findById(placeId).get();
        Session session = sessionRepository.findById(sessionId).get();
        User user = userRepository.findById(userId).get();
        Ticket ticket = ticketMapper.convertToTicket(ticketDto, session, user, place);
//        ticket.setTicketCode(generateTicketCode());
        return ticketRepository.save(ticket);
    }

    public List<Place> findTakenPlacesByHallId(Long hallId) {
        return ticketRepository.findAllPlacesByHallId(hallId);
    }

    public Ticket updateTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public Page<Ticket> findByUserName(String userName, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        return ticketRepository.findByUserUsername(userName, pageable);
    }

//    public String generateTicketCode() { todo: Implement in future
//        String ticketCode = UUID.randomUUID().toString().substring(0, 10);
//        List<Ticket> ticketsWithCodeCollision = ticketRepository.findByTicketCode(ticketCode);
//        if(ticketsWithCodeCollision.isEmpty()){
//            return ticketCode;
//        }
//        return generateTicketCode();
//    }
}

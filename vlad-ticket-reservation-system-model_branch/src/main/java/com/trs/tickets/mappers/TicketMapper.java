package com.trs.tickets.mappers;

import com.trs.tickets.model.dto.PlaceDto;
import com.trs.tickets.model.dto.SessionDto;
import com.trs.tickets.model.dto.TicketDto;
import com.trs.tickets.model.entity.*;
import com.trs.tickets.repository.TicketRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper extends GenericMapper<Ticket, TicketDto> {

    @Override
    @Mapping(target = "userId", source = "user")
    @Mapping(target = "placeId", source = "place")
    @Mapping(target = "sessionId", source = "session")
    TicketDto convert(Ticket ticket);

    default Long map(User user) {
        return user.getId();
    }

    default Long map(Place place) {
        return place.getId();
    }

    default Long map(Session session) {
        return session.getId();
    }

    default Ticket convertToTicket(TicketDto ticketDto, Session session, User user, Place place){
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setSession(session);
        ticket.setPlace(place);
        ticket.setPrice(ticketDto.getPrice());
        return ticket;
    }
}

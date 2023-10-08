package com.trs.tickets.repository;

import com.trs.tickets.model.entity.Place;
import com.trs.tickets.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT DISTINCT t.place FROM Ticket t WHERE t.session.hall.id = :hallId")
    List<Place> findAllPlacesByHallId(@Param("hallId") Long hallId);
    List<Ticket> findByUserUsername(String userName);
    List<Ticket> findAllByUserId(Long id);
    List<Ticket> findAllBySessionId(Long sessionId);
//    List<Ticket> findByTicketCode(String ticketCode);
}

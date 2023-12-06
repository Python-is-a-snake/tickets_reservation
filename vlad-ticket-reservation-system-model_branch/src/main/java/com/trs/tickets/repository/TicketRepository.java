package com.trs.tickets.repository;

import com.trs.tickets.model.entity.Place;
import com.trs.tickets.model.entity.Ticket;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT DISTINCT t.place FROM Ticket t WHERE t.session.hall.id = :hallId")
    List<Place> findAllPlacesByHallId(@Param("hallId") Long hallId);
    List<Ticket> findByUserUsername(String userName);
    List<Ticket> findAllByUserId(Long id);
    List<Ticket> findAllBySessionId(Long sessionId);
//    List<Ticket> findByTicketCode(String ticketCode);

    @Query("DELETE FROM Ticket t WHERE t.session.id = :sessionId")
    @Modifying
    @Transactional
    void deleteTicketsBySessionId(@Param("sessionId") Long sessionId);

    @Query("DELETE FROM Place p WHERE p.session.id = :sessionId")
    @Modifying
    @Transactional
    void deletePlacesBySessionId(@Param("sessionId") Long sessionId);

    //CHARTS
    @Query("SELECT new map(m.title as title, COUNT(t) as ticketCount) " +
            "FROM Ticket t " +
            "JOIN t.session s " +
            "JOIN s.movie m " +
            "GROUP BY m.title")
    List<Map<String, Object>> countTicketsByMovieTitle();

    @Query("SELECT new map(m.title as title, SUM(t.price) as totalSales) " +
            "FROM Ticket t " +
            "JOIN t.session s " +
            "JOIN s.movie m " +
            "GROUP BY m.title")
    List<Map<String, Object>> sumTicketSalesByMovie();


    // In TicketRepository.java
    @Query("SELECT new map(" +
            "CASE " +
            "WHEN FUNCTION('HOUR', s.sessionDateTime) < 12 THEN 'Morning' " +
            "WHEN FUNCTION('HOUR', s.sessionDateTime) >= 12 AND FUNCTION('HOUR', s.sessionDateTime) < 17 THEN 'Afternoon' " +
            "WHEN FUNCTION('HOUR', s.sessionDateTime) >= 17 AND FUNCTION('HOUR', s.sessionDateTime) < 21 THEN 'Evening' " +
            "ELSE 'Night' END as timeSlot, COUNT(t) as ticketCount) " +
            "FROM Ticket t " +
            "JOIN t.session s " +
            "GROUP BY timeSlot")
    List<Map<String, Object>> countTicketsByTimeSlot();

    @Query("SELECT new map(m.title as movieTitle, SUM(t.price) as totalRevenue) " +
            "FROM Ticket t " +
            "JOIN t.session s " +
            "JOIN s.movie m " +
            "GROUP BY m.title")
    List<Map<String, Object>> totalRevenueByMovie();

    @Query("SELECT new map(" +
            "CASE " +
            "WHEN m.duration < 60 THEN 'Short (< 1 hour)' " +
            "WHEN m.duration >= 60 AND m.duration < 120 THEN 'Medium (1-2 hours)' " +
            "WHEN m.duration >= 120 AND m.duration < 180 THEN 'Long (2-3 hours)' " +
            "ELSE 'Very Long (> 3 hours)' END as durationRange, " +
            "SUM(t.price) as totalSales) " +
            "FROM Ticket t " +
            "JOIN t.session s " +
            "JOIN s.movie m " +
            "GROUP BY durationRange")
    List<Map<String, Object>> sumTicketSalesByDuration();

    @Query("SELECT new map(" +
            "FUNCTION('DAYNAME', s.sessionDateTime) as dayOfWeek, " +
            "SUM(t.price) as totalSales) " +
            "FROM Ticket t " +
            "JOIN t.session s " +
            "GROUP BY dayOfWeek")
    List<Map<String, Object>> sumTicketSalesByDayOfWeek();

}

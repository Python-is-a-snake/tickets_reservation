package com.trs.tickets.repository;

import com.trs.tickets.model.dto.projection.MovieSessionsProjection;
import com.trs.tickets.model.dto.projection.MoviesTicketsBoughtThisMonthProjection;
import com.trs.tickets.model.dto.projection.SessionsByDayOfWeekProjection;
import com.trs.tickets.model.dto.projection.TicketCountByHour;
import com.trs.tickets.model.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ChartsRepository extends JpaRepository<Session, Long> {

    @Query(value = "SELECT m.title AS title, COUNT(s.id) AS sessionCount FROM movie m JOIN session s ON m.id = s.movie_id GROUP BY m.title ORDER BY m.title", nativeQuery = true)
    List<MovieSessionsProjection> findMoviesAndSessionCounts();

    @Query(value = "SELECT COUNT(s.id) AS sessionCount FROM movie m JOIN session s ON m.id = s.movie_id WHERE s.is_active IS FALSE GROUP BY m.title ORDER BY m.title", nativeQuery = true)
    List<Integer> findInactiveSessionCounts();

    @Query(value = "SELECT m.title AS title, COUNT(t.id) AS ticketsCount " +
            "FROM movie m " +
            "JOIN session s ON m.id = s.movie_id " +
            "JOIN ticket t ON s.id = t.session_id " +
            "WHERE t.purchase_date BETWEEN :startOfMonth AND :now " +
            "GROUP BY m.title", nativeQuery = true)
    List<MoviesTicketsBoughtThisMonthProjection> findTicketsBoughtForMoviesThisMonth(@Param("startOfMonth") LocalDate startOfMonth, @Param("now") LocalDate now);

    @Query(value = "SELECT HOUR(s.session_date_time) AS hourOfDay, COUNT(t.id) AS ticketsCount " +
            "FROM session s " +
            "JOIN ticket t ON s.id = t.session_id " +
            "GROUP BY HOUR(s.session_date_time) " +
            "ORDER BY hourOfDay",
            nativeQuery = true)
    List<TicketCountByHour> findTicketsCountByHour();

//    @Query(value = "SELECT DAYNAME(s.sessionDateTime) AS daysOfWeek, COUNT(*)" +
//            "FROM Session s GROUP BY 1")
//    List<SessionsByDayOfWeekProjection> findSessionsForDaysOfWeek(@Param("startOfMonth") LocalDate startOfMonth, @Param("now") LocalDate now);

    @Query(value = "SELECT" +
            "  CASE DAYOFWEEK(s.session_date_time)" +
            "    WHEN 1 THEN 'Sunday'" +
            "    WHEN 2 THEN 'Monday'" +
            "    WHEN 3 THEN 'Tuesday'" +
            "    WHEN 4 THEN 'Wednesday'" +
            "    WHEN 5 THEN 'Thursday'" +
            "    WHEN 6 THEN 'Friday'" +
            "    ELSE 'Saturday'" +
            "  END AS dayOfWeek," +
            "  COUNT(*) AS sessionsCount" +
            " FROM session s" +
            " WHERE s.session_date_time BETWEEN :startOfMonth AND :now " +
            " GROUP BY DAYOFWEEK(s.session_date_time)" +
            " ORDER BY CASE DAYOFWEEK(s.session_date_time)" +
            "         WHEN 2 THEN 1" +
            "         ELSE DAYOFWEEK(s.session_date_time) END", nativeQuery = true)
    List<SessionsByDayOfWeekProjection> findSessionsForDaysOfWeek(@Param("startOfMonth") LocalDate startOfMonth, @Param("now") LocalDate now);

}

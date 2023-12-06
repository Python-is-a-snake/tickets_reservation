package com.trs.tickets.repository;

import com.trs.tickets.model.entity.Place;
import com.trs.tickets.model.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByMovieId(Long movieId);

    @Query("SELECT new map(h.name as hallName, COUNT(s) as sessionCount) " +
            "FROM Session s " +
            "JOIN s.hall h " +
            "GROUP BY h.name")
    List<Map<String, Object>> countSessionsByHall();


}

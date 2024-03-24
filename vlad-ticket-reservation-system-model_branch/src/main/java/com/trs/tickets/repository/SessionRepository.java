package com.trs.tickets.repository;

import com.trs.tickets.model.entity.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    List<Session> findAllBySessionDateTimeLessThanOrderBySessionDateTimeAsc(LocalDateTime localDateTime);

    Page<Session> findAllByOrderBySessionDateTimeDesc(Pageable pageable);

}

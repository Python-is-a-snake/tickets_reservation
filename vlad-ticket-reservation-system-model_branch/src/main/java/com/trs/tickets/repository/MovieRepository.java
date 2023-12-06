package com.trs.tickets.repository;

import com.trs.tickets.model.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);


    @Query("SELECT new map(m.genre as genre, AVG(m.duration) as avgDuration) " +
            "FROM Ticket t " +
            "JOIN t.session s " +
            "JOIN s.movie m " +
            "GROUP BY m.genre")
    List<Map<String, Object>> averageMovieDurationByGenre();

}

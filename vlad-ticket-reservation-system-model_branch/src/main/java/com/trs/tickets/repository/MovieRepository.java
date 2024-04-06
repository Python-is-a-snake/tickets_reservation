package com.trs.tickets.repository;

import com.trs.tickets.model.dto.projection.MovieMostSessionsProjection;
import com.trs.tickets.model.dto.projection.MovieSessionsProjection;
import com.trs.tickets.model.entity.Movie;
import org.hibernate.annotations.OrderBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT m.id AS id, m.title AS title, COUNT(s.id) as sessionCount FROM Movie m JOIN Session s ON m.id = s.movie.id GROUP BY m.title ORDER BY COUNT(s.id) DESC LIMIT 10")
    List<MovieMostSessionsProjection> findMoviesWithMostSessions();

}

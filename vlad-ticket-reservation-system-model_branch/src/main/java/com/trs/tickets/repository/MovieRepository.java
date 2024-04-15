package com.trs.tickets.repository;

import com.trs.tickets.model.dto.projection.MovieMostSessionsProjection;
import com.trs.tickets.model.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    //For MAIN page
    Page<Movie> findAllByIsActiveTrue(Pageable pageable);
    List<Movie> findAllByIsActiveTrue();
    Page<Movie> findByTitleContainingIgnoreCaseAndIsActiveTrue(String title, Pageable pageable);
    List<Movie> findByTitleContainingIgnoreCase(String title);

    @Query(value = "SELECT * from movie JOIN rating r on movie.id = r.movie_id ORDER BY score DESC LIMIT 10", nativeQuery = true)
    List<Movie> find10MoviesWithMostScore();

    //For ADMIN page
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT m.id AS id, m.title AS title, COUNT(s.id) as sessionCount FROM Movie m JOIN Session s ON m.id = s.movie.id WHERE s.isActive GROUP BY m.title ORDER BY COUNT(s.id) DESC LIMIT 10")
    List<MovieMostSessionsProjection> findMoviesWithMostSessions();

}

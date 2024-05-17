package com.trs.tickets.repository;

import com.trs.tickets.model.entity.Movie;
import com.trs.tickets.model.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Rating findByUserIdAndMovieId(Long userId, Long movieId);

    List<Rating> findByMovieId(Long movieId);
}
package com.trs.tickets.service;

import com.trs.tickets.model.dto.RatingDto;
import com.trs.tickets.model.entity.Movie;
import com.trs.tickets.model.entity.Rating;
import com.trs.tickets.model.entity.User;
import com.trs.tickets.repository.MovieRepository;
import com.trs.tickets.repository.RatingRepository;
import com.trs.tickets.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService {
    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public List<Rating> findByMovieId(Long movieId){
        return ratingRepository.findByMovieId(movieId);
    }

    public Rating findByUserAndMovieId(Long userId, Long movieId){
        Rating rating = ratingRepository.findByUserIdAndMovieId(userId, movieId);
        if(rating == null){
            rating =  new Rating();
            rating.setScore((short) 0); // set Score to 0 to display all starts 'unchecked' in id-movie page
        }
        return rating;
    }

    public Rating saveRating(RatingDto ratingDto){
        Rating rating = new Rating();

        Long userId = ratingDto.getUserId();
        Long movieId = ratingDto.getMovieId();

        Rating potentiallyExistingRating = ratingRepository.findByUserIdAndMovieId(userId, movieId);

        if(userId != null && movieId != null){
            User user = userRepository.findById(userId).get();
            Movie movie = movieRepository.findById(movieId).get();
            rating.setUser(user);
            rating.setMovie(movie);
            rating.setScore(ratingDto.getScore());
        }

        //Rewrite Rating
        if(potentiallyExistingRating != null){
            log.info("Deleting Rating(userId:{}, movieId:{}, score:{})", potentiallyExistingRating.getUser().getId(), potentiallyExistingRating.getMovie().getId(), potentiallyExistingRating.getScore())   ;
            ratingRepository.delete(potentiallyExistingRating);
            ratingRepository.flush();
        }

        log.info("Saving Rating: {}", ratingDto);
        return ratingRepository.save(rating);
    }

    public Double getAverageRatingForMovie(Long movieId) {
        List<Rating> ratingsForMovie = findByMovieId(movieId);
        double scoreSum = ratingsForMovie.stream().mapToInt(Rating::getScore).sum();
        int scoreCount = ratingsForMovie.size();
        return scoreSum / scoreCount;
    }

    public Map<Short, Long> getScoresCountMapForMovie(Long movieId){
        List<Rating> ratingsForMovie = findByMovieId(movieId);
        return ratingsForMovie.stream().collect(Collectors.groupingBy(Rating::getScore, Collectors.counting()));
    }
}

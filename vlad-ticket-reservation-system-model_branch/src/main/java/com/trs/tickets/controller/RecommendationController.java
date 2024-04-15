package com.trs.tickets.controller;

import com.trs.tickets.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/recommendations/{userId}")
    public List<RecommendedItem> getRecommendedMovies(@PathVariable Long userId) {
        return recommendationService.recommendMoviesForUser(userId); // 10 recommendations
    }
}

package com.trs.tickets.controller;

import com.trs.tickets.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/recommendations/{userId}")
    public String getRecommendedMovies(@PathVariable Long userId, Model model) {
        model.addAttribute("recommendedMovies", recommendationService.recommendMoviesForUser(userId));// 10 recommendations
        return "main/movies-page";
    }
}

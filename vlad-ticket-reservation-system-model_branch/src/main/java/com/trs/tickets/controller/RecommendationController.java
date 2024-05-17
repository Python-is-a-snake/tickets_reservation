package com.trs.tickets.controller;

import com.trs.tickets.service.RecommendationService;
import com.trs.tickets.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final UserService userService;

    @GetMapping("/recommendations")
    public String getRecommendedMovies(Model model, Authentication authentication) {
        Long userId = userService.findByUsername(authentication.getName()).getId();
        model.addAttribute("movies", recommendationService.recommendMoviesForUser(userId));// 10 recommendations
        return "main/movies-page";
    }
}

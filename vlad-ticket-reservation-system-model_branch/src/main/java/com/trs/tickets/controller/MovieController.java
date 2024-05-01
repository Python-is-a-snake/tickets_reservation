package com.trs.tickets.controller;

import com.trs.tickets.model.dto.MovieDto;
import com.trs.tickets.model.entity.Rating;
import com.trs.tickets.repository.RatingRepository;
import com.trs.tickets.repository.UserRepository;
import com.trs.tickets.service.MovieService;
import com.trs.tickets.service.PageSizeCheckerService;
import com.trs.tickets.service.RatingService;
import com.trs.tickets.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.extras.springsecurity6.auth.Authorization;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final UserService userService;
    private final RatingService ratingService;
    private final PageSizeCheckerService pageSizeCheckerService;

    //USER & Anonymous User

    //view All movies - MAIN PAGE
    @GetMapping
    public String getMovies(Model model,
                               @RequestParam(name = "title", required = false) String title,
                               @RequestParam(name = "page", defaultValue = "0") Integer page,
                               @RequestParam(name = "size", defaultValue = "6") Integer size,
                            Authentication authentication
                               ) {

        page = pageSizeCheckerService.checkPage(page);
        size = pageSizeCheckerService.checkSize(size);

        Page<MovieDto> moviesPage;

        if(title == null || title.isBlank()){
            moviesPage = movieService.getActiveMovies(page, size);
            model.addAttribute("title", null);
        }else {
            moviesPage = movieService.getActiveMoviesByTitle(title, page, size);
            model.addAttribute("title", title);
        }
        model.addAttribute("movies", moviesPage.getContent());
        model.addAttribute("currentPage", moviesPage.getNumber());
        model.addAttribute("totalItems", moviesPage.getTotalElements());
        model.addAttribute("totalPages", moviesPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "main/movies-page";
    }

//    @GetMapping("/{id}")
//    public String getMovieInfoPage(@PathVariable("id") Long id, Model model) {
//        MovieDto movie = movieService.getMovieById(id);
//        model.addAttribute("movie", movie);
//
//        model.addAttribute("sessions", movie.getSessionsGroupedByDateTime());
//        return "movie-session/id-movie-page";
//    }


    @GetMapping("/{id}")
    public String getMovieInfoPage(@PathVariable("id") Long id, Model model, Authentication authentication) {
        MovieDto movie = movieService.getMovieById(id);
        List<Rating> ratingsForCurrentMovie = ratingService.findByMovieId(movie.getId());

        model.addAttribute("ratings", ratingsForCurrentMovie);
        model.addAttribute("movie", movie);
        model.addAttribute("sessions", movie.getSessionsGroupedByDateTime());

        if(authentication == null){
            model.addAttribute("userId", null);
            model.addAttribute("currentUserRating", null);
        }else {
            Long userId = userService.getUserByUsername(authentication.getName()).getId();
            model.addAttribute("userId", userId);
            model.addAttribute("username", userService.getUserById(userId).getUsername());
            model.addAttribute("currentUserRating", ratingService.findByUserAndMovieId(userId, movie.getId()).getScore());
        }

        return "movie-session/new_id_movie";
    }

    //ADMIN
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String getMoviesAdminPage(Model model,
                                            @RequestParam(name = "title", required = false) String title,
                                            @RequestParam(name = "page", defaultValue = "0") Integer page,
                                            @RequestParam(name = "size", defaultValue = "6") Integer size) {

        page = pageSizeCheckerService.checkPage(page);
        size = pageSizeCheckerService.checkSize(size);

        Page<MovieDto> moviesPage;
        if(title == null || title.isBlank()){
            moviesPage = movieService.getAllMovies(page, size);
            model.addAttribute("title", null);
        }else {
            moviesPage = movieService.getMoviesByTitle(title, page, size);
            model.addAttribute("title", title);
        }

        model.addAttribute("movies", moviesPage.getContent());
        model.addAttribute("currentPage", moviesPage.getNumber());
        model.addAttribute("totalItems", moviesPage.getTotalElements());
        model.addAttribute("totalPages", moviesPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "admin/admin-movies";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String createMovie(Model model) {
        MovieDto movie = new MovieDto();
        model.addAttribute("movie", movie);
        return "admin/create-movie-page";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String createMoviePost(@Valid @ModelAttribute("movie") MovieDto movie, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("movie", movie);
            return "admin/create-movie-page";
        }
        movieService.createMovie(movie);
        return "redirect:/movies/all";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String editMovie(@PathVariable("id") Long id, Model model) {
        MovieDto movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);
        return "admin/edit-movie-page";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String editMoviePost(@PathVariable("id") Long id, @Valid @ModelAttribute("movie") MovieDto movie,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/edit-movie-page";
        }

        LocalDate date = LocalDate.parse(String.valueOf(movie.getReleaseDate()), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        movie.setId(id);
        movie.setReleaseDate(date);
        movieService.updateMovie(movie);
        return "redirect:/movies/all";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String deleteMovie(@PathVariable("id") Long id) {
        movieService.deleteMovie(id);
        return "redirect:/movies/all";
    }

    @PostMapping("/deactivate/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String inactiveMovie(@PathVariable("id") Long id) {
        movieService.deactivateMovie(id);
        return "redirect:/movies/all";
    }
}

package com.trs.tickets.controller;

import com.trs.tickets.model.dto.MovieDto;
import com.trs.tickets.model.entity.Movie;
import com.trs.tickets.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    //USER & Anonymous User

    //view All movies - MAIN PAGE
    @GetMapping
    public String getAllMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "movies-page";
    }

    //view All movies with SEARCH - MAIN PAGE
    @GetMapping("/search")
    public String getMoviesByTitle(@RequestParam(name = "title", required = false) String title, Model model) {
        model.addAttribute("movies", movieService.getMoviesByTitle(title));
        return "movies-page";
    }


    //ADMIN

    //view all Movies admin page (with buttons to VIEW, EDIT, DELETE)
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String getAllMoviesAdminPage(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "admin-movies";
    }

    //view all Movies admin page with SEARCH by Movie title (with buttons to VIEW, EDIT, DELETE)
    @GetMapping("/all/search")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String getMoviesByTitleAdminPage(@RequestParam(name = "title", required = false) String title, Model model) {
        model.addAttribute("movies", movieService.getMoviesByTitle(title));
        return "admin-movies";
    }


    //
    @GetMapping("/{id}")
    public String getMovieInfoPage(@PathVariable("id") Long id, Model model) {
        MovieDto movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);

        model.addAttribute("sessions", movie.getSessionsGroupedByDateTime());
        return "id-movie-page";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String createMovie(Model model) {
        MovieDto movie = new MovieDto();
        model.addAttribute("movie", movie);
        return "create-movie-page";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String createMoviePost(@Valid @ModelAttribute("movie") MovieDto movie, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("movie", movie);
            return "create-movie-page";
        }
        movieService.addMovie(movie);
        return "redirect:/movies";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String editMovie(@PathVariable("id") Long id, Model model) {
        MovieDto movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);
        return "edit-movie-page";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String editMoviePost(@PathVariable("id") Long id, @Valid @ModelAttribute("movie") MovieDto movie,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-movie-page";
        }

        LocalDate date = LocalDate.parse(String.valueOf(movie.getReleaseDate()), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        movie.setId(id);
        movie.setReleaseDate(date);
        movieService.updateMovie(movie);
        return "redirect:/movies";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String deleteMovie(@PathVariable("id") Long id) {
        movieService.deleteMovie(id);
        return "redirect:/movies";
    }


    @GetMapping("/pageable")
    public String getPagedMovies(Model model, @RequestParam(name = "page", defaultValue = "0") Integer page,
                                 @RequestParam(name = "size", defaultValue = "6") Integer size){

        Page<Movie> moviesPage = movieService.getMoviesPage(page, size);

        model.addAttribute("movies", moviesPage.getContent());
        model.addAttribute("currentPage", moviesPage.getNumber());
        model.addAttribute("totalItems", moviesPage.getTotalElements());
        model.addAttribute("totalPages", moviesPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "movies-paged";
    }
}

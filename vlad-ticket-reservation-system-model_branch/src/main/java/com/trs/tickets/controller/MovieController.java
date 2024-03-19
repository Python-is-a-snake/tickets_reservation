package com.trs.tickets.controller;

import com.trs.tickets.model.dto.MovieDto;
import com.trs.tickets.service.MovieService;
import com.trs.tickets.service.PageSizeCheckerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final PageSizeCheckerService pageSizeCheckerService;

    //USER & Anonymous User

    //view All movies - MAIN PAGE
    @GetMapping
    public String getMovies(Model model,
                               @RequestParam(name = "title", required = false) String title,
                               @RequestParam(name = "page", defaultValue = "0") Integer page,
                               @RequestParam(name = "size", defaultValue = "6") Integer size
                               ) {

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

        return "main/movies-page";
    }

    //view All movies with SEARCH - MAIN PAGE
//    @GetMapping("/search")
//    public String getMoviesByTitle(Model model,
//                                   @RequestParam(name = "title", required = false) String title,
//                                   @RequestParam(name = "page", defaultValue = "0") Integer page,
//                                   @RequestParam(name = "size", defaultValue = "6") Integer size) {
//
//        page = pageSizeCheckerService.checkPage(page);
//        size = pageSizeCheckerService.checkSize(size);
//
//        Page<MovieDto> moviesPage = movieService.getMoviesByTitle(title, page, size);
//
//        model.addAttribute("movies", moviesPage.getContent());
//        model.addAttribute("currentPage", moviesPage.getNumber());
//        model.addAttribute("totalItems", moviesPage.getTotalElements());
//        model.addAttribute("totalPages", moviesPage.getTotalPages());
//        model.addAttribute("pageSize", size);
//
//        return "main/movies-page";
//    }

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

    //view all Movies admin page with SEARCH by Movie title (with buttons to VIEW, EDIT, DELETE)
//    @GetMapping("/all/search")
//    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
//    public String getMoviesByTitleAdminPage(Model model,
//                                            @RequestParam(name = "title", required = false) String title,
//                                            @RequestParam(name = "page", defaultValue = "0") Integer page,
//                                            @RequestParam(name = "size", defaultValue = "6") Integer size) {
//
//        page = pageSizeCheckerService.checkPage(page);
//        size = pageSizeCheckerService.checkSize(size);
//
//
//        Page<MovieDto> moviesPage = movieService.getMoviesByTitle(title, page, size);
//
//        model.addAttribute("movies", moviesPage.getContent());
//        model.addAttribute("currentPage", moviesPage.getNumber());
//        model.addAttribute("totalItems", moviesPage.getTotalElements());
//        model.addAttribute("totalPages", moviesPage.getTotalPages());
//        model.addAttribute("pageSize", size);
//
//        return "admin/admin-movies";
//    }

    //
    @GetMapping("/{id}")
    public String getMovieInfoPage(@PathVariable("id") Long id, Model model) {
        MovieDto movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);

        model.addAttribute("sessions", movie.getSessionsGroupedByDateTime());
        return "movie-session/id-movie-page";
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
        return "redirect:/movies";
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
        return "redirect:/movies";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String deleteMovie(@PathVariable("id") Long id) {
        movieService.deleteMovie(id);
        return "redirect:/movies";
    }
}

package com.trs.tickets.controller;

import com.trs.tickets.mappers.SessionMapper;
import com.trs.tickets.mappers.UserMapper;
import com.trs.tickets.model.dto.MovieDto;
import com.trs.tickets.model.dto.SessionDto;
import com.trs.tickets.model.dto.TicketDto;
import com.trs.tickets.model.dto.UserDto;
import com.trs.tickets.model.entity.Hall;
import com.trs.tickets.model.entity.Place;
import com.trs.tickets.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class SessionController {
    private final SessionService sessionService;
    private final MovieService movieService;
    private final HallService hallService;
    private final TicketService ticketService;
    private final PlaceService placeService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final SessionMapper sessionMapper;

    // USER

    //get page with seats display
    @GetMapping("/buy-tickets/{sessionId}")
    @PreAuthorize("hasAuthority('USER')")
    public String getSessionPage(@PathVariable("sessionId") Long sessionId, Model model) {
        SessionDto session = sessionService.getSessionById(sessionId);
        MovieDto movie = movieService.getMovieById(session.getMovieId());
        Hall hall = hallService.getHallById(session.getHallId());

        model.addAttribute("movieSession", session);
        model.addAttribute("movie", movie);
        model.addAttribute("hall", hall);

        Map<Integer, List<Place>> places = session.getPlaces().stream().collect(Collectors.groupingBy(Place::getRow));
        model.addAttribute("places", places);

        List<Place> takenPlaces = ticketService.findTakenPlacesByHallId(hall.getId());
        model.addAttribute("takenPlaces", takenPlaces);
        return "buy-tickets-page";
    }

    //clicked on Place - go to purchase page
    @PostMapping("/buy-tickets/{sessionId}")
    @PreAuthorize("hasAuthority('USER')")
    public String buyTicketPage(@PathVariable("sessionId") Long sessionId,
                                @RequestParam(name = "placeId", required = false) Long placeId,
                                Model model,
                                Authentication authentication) {
        SessionDto session = sessionService.getSessionById(sessionId);
        Place place = placeService.getPlaceById(placeId);
        UserDto user = userService.getUserByUsername(authentication.getName()).get(0);
        TicketDto ticket = new TicketDto();

        ticket.setSessionId(session.getId());
        ticket.setPlaceId(place.getId());
        ticket.setPrice(place.getPlaceType().getPrice());
        ticket.setUserId(user.getId());

        model.addAttribute("movieSession", session);
        model.addAttribute("movie", movieService.getMovieById(session.getMovieId()));
        model.addAttribute("place", place);
        model.addAttribute("hall", hallService.getHallById(session.getHallId()));
        model.addAttribute("ticket", ticket);
        model.addAttribute("user", user);

        return "ticket-purchase-page";
    }

    // ADMIN

    //view all Sessions page (with buttons to VIEW, EDIT, DELETE)
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String allSessionsAdminPage(Model model) {
        model.addAttribute("movieSessions", sessionService.getAllSessions());
        return "admin-sessions";
    }

    //view Session creation page
    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String createSession(Model model) {
        SessionDto session = new SessionDto();
        model.addAttribute("movieSession", session);
        model.addAttribute("halls", hallService.getAllHalls());
        model.addAttribute("movies", movieService.getAllMovies());
        return "create-session-page";
    }

    //create Session
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String createSessionPost(@Valid @ModelAttribute("movieSession") SessionDto session,
                                  @RequestParam(name = "movieId") Long movieId,
                                  @RequestParam("hallId") Long hallId,
                                  BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("movieSession", session);
            return "create-session-page";
        }

        sessionService.createSession(movieId, hallId, session);
        return "redirect:/movies";
    }

    //view Session edit page
    @GetMapping("/edit/{sessionId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String editSession(@PathVariable("sessionId") Long sessionId, Model model) {
        SessionDto sessionDto = sessionService.getSessionById(sessionId);
        model.addAttribute("sessionDto", sessionDto);
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("halls", hallService.getAllHalls());
        return "edit-session-page";
    }

    //edit Session
    @PostMapping("/edit")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String updateSession(@ModelAttribute("sessionDto") SessionDto sessionDto) {
        sessionService.updateSession(sessionDto);
        return "redirect:/sessions";
    }

    //delete Session
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String deleteSession(@PathVariable("id") Long id) {
        sessionService.deleteSession(id);
        return "redirect:/sessions/all";
    }

}

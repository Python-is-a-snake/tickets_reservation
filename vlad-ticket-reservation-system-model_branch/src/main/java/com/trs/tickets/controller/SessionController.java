package com.trs.tickets.controller;

import com.trs.tickets.model.dto.MovieDto;
import com.trs.tickets.model.dto.SessionDto;
import com.trs.tickets.model.dto.TicketDto;
import com.trs.tickets.model.dto.UserDto;
import com.trs.tickets.model.entity.Hall;
import com.trs.tickets.model.entity.Place;
import com.trs.tickets.model.entity.Session;
import com.trs.tickets.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
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

    private final PageSizeCheckerService pageSizeCheckerService;

    // USER

    //get page with seats display
    @GetMapping("/buy-tickets/{sessionId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'CEO')")
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
        return "buy-ticket/buy-tickets-page";
    }

    //clicked on Place - go to purchase page
    @PostMapping("/buy-tickets/{sessionId}")
    @PreAuthorize("hasAnyAuthority('CEO', 'ADMIN', 'USER')")
    public String buyTicketPage(@PathVariable("sessionId") Long sessionId,
                                @RequestParam(name = "placeId", required = false) Long placeId,
                                Model model,
                                Authentication authentication) {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for(GrantedAuthority authority : authorities){
            if(authority.getAuthority().equals("CEO") || authority.getAuthority().equals("ADMIN")){
                throw new AccessDeniedException("Admin or CEO can not buy a ticket!");
            }
        }

        SessionDto session = sessionService.getSessionById(sessionId);
        Place place = placeService.getPlaceById(placeId);
        UserDto user = userService.getUserByUsername(authentication.getName());
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

        return "buy-ticket/ticket-purchase-page";
    }

    // ADMIN

    //view all Sessions page (with buttons to VIEW, EDIT, DELETE)
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String allSessionsAdminPage(Model model,
                                       @RequestParam(name = "page", defaultValue = "0") Integer page,
                                       @RequestParam(name = "size", defaultValue = "6") Integer size) {

        page = pageSizeCheckerService.checkPage(page);
        size = pageSizeCheckerService.checkSize(size);

        Page<Session> sessionPage = sessionService.getAllSessions(page, size);

        model.addAttribute("movieSessions", sessionPage.getContent());
        model.addAttribute("currentPage", sessionPage.getNumber());
        model.addAttribute("totalItems", sessionPage.getTotalElements());
        model.addAttribute("totalPages", sessionPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "admin/admin-sessions";
    }

    //view Session creation page
    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String createSession(Model model) {
        SessionDto session = new SessionDto();
        model.addAttribute("movieSession", session);
        model.addAttribute("halls", hallService.getAllHalls());
        model.addAttribute("movies", movieService.getAllMovies());
        return "admin/create-session-page";
    }

    //create Session
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String createSessionPost(@Valid @ModelAttribute("movieSession") SessionDto session,
                                  @RequestParam(name = "movieId") Long movieId,
                                  @RequestParam("hallId") Long hallId,
                                  BindingResult bindingResult, Model model) {

        if(session.getSessionDateTime() == null){
            bindingResult.addError(new FieldError("movieSession", "sessionDateTime", "Session Date and Time can not be empty!"));
        }else if(session.getSessionDateTime().isBefore(LocalDateTime.now())){
            bindingResult.addError(new FieldError("movieSession","sessionDateTime", "Session Date and Time must be in future time!"));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("movieSession", session);
            model.addAttribute("halls", hallService.getAllHalls());
            model.addAttribute("movies", movieService.getAllMovies());
            return "admin/create-session-page";
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
        return "admin/edit-session-page";
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

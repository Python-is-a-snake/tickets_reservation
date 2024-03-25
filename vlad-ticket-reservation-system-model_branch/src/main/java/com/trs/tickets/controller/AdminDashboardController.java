package com.trs.tickets.controller;

import com.trs.tickets.model.entity.Session;
import com.trs.tickets.service.MovieService;
import com.trs.tickets.service.SessionService;
import com.trs.tickets.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminDashboardController {
    private final SessionService sessionService;
    private final MovieService movieService;
    private final UserService userService;
    @GetMapping("/test")
    public String test(Model model){
        //show unavailable sessions
        List<Session> allNotAvailableSessionsSortedByDateTime = sessionService.getAllNotAvailableSessionsSortedByDateTime();
        model.addAttribute("unavailableSessions", allNotAvailableSessionsSortedByDateTime);
        model.addAttribute("dateUtils", new DateUtils());

        //data for 4 cards

        //count total movies amount
        Long totalMovies = movieService.getAllMovies().stream().count();
        model.addAttribute("totalMovies", totalMovies);

        //count total sessions amount
        Long totalSessions = sessionService.getAllSessions().stream().count();
        model.addAttribute("totalSessions", totalSessions);

        //count total users amount
        Long totalUsers = userService.getAllUsers().stream().count();
        model.addAttribute("totalUsers", totalUsers);

        //count new users amount of this Month
        Long newUsers = userService.getNewUsersOfThisMonth().stream().count();
        model.addAttribute("newUsers", newUsers);

        return "/admin/test";
    }

}

class DateUtils {
    public static long daysBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.DAYS.between(start, end);
    }
}

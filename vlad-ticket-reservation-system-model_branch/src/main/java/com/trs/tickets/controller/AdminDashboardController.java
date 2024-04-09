package com.trs.tickets.controller;

import com.trs.tickets.model.dto.projection.*;
import com.trs.tickets.model.entity.Session;
import com.trs.tickets.service.ChartsService;
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
    private final ChartsService chartsService;
    @GetMapping("/test")
    public String test(Model model){
        //show unavailable sessions - SECTION 1
        List<Session> allNotAvailableSessionsSortedByDateTime = sessionService.getAllNotAvailableSessionsSortedByDateTime();
        model.addAttribute("unavailableSessions", allNotAvailableSessionsSortedByDateTime);
        model.addAttribute("dateUtils", new DateUtils());

        //data for 4 cards - SECTIONS 2 - 3
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

        //Movies with most Sessions - SECTION 4
        List<MovieMostSessionsProjection> moviesWithMostSessions = movieService.getMoviesWithMostSessions();
        model.addAttribute("moviesWithMostSessions", moviesWithMostSessions);

        // == CHARTS ==

        //SECTION 5
        //CHART 1
        //Displays movies titles and amount of sessions for each movie
        addChartMoviesSessionCountData(model);

        addChartCountBoughtTicketsForMoviesThisMonthData(model);

        addChartTicketsCountByHourData(model);

        addChartSessionsForDaysOfWeekData(model);

        return "/admin/test";
    }

    //CHART 1
    //Displays movies titles and amount of sessions for each movie
    private void addChartMoviesSessionCountData(Model model) {
        List<MovieSessionsProjection> moviesSessionsCount = chartsService.getMoviesSessionsCount();

        //Amount of sessions for each Movie
        List<Integer> moviesSessionsPlotSessionsAmount = moviesSessionsCount.stream()
                .map(MovieSessionsProjection::getSessionCount)
                .toList();


        //Movies titles
        List<String> moviesSessionsPlotSessionsTitles = moviesSessionsCount.stream()
                .map(MovieSessionsProjection::getTitle)
                .toList();

        model.addAttribute("chart_moviesSessionsCount", moviesSessionsPlotSessionsAmount);//chart data
        //Amount of Inactive sessions for each Movie
        model.addAttribute("chart_moviesInactiveSessionsCount", chartsService.findInactiveSessionCounts());//chart inactive sessions data TODO
        model.addAttribute("chart_moviesSessionsTitles", moviesSessionsPlotSessionsTitles);//chart labels
    }

    //CHART 2
    //Displays movies titles and amount of sessions for each movie
    private void addChartCountBoughtTicketsForMoviesThisMonthData(Model model) {
        List<MoviesTicketsBoughtThisMonthProjection> ticketsBoughtForMoviesThisMonth = chartsService.findTicketsBoughtForMoviesThisMonth();

        //Amount of tickets bought for each Movie this Month
        List<Integer> ticketsBoughtThisMonthAmount = ticketsBoughtForMoviesThisMonth.stream()
                .map(MoviesTicketsBoughtThisMonthProjection::getTicketsCount)
                .toList();

        //Movies titles
        List<String> movieTitlesForBoughtTickets = ticketsBoughtForMoviesThisMonth.stream()
                .map(MoviesTicketsBoughtThisMonthProjection::getTitle)
                .toList();

        model.addAttribute("chart_ticketsThisMonthCount", ticketsBoughtThisMonthAmount);//chart data
        model.addAttribute("chart_ticketsThisMonthTitles", movieTitlesForBoughtTickets);//chart labels
    }

    private void addChartTicketsCountByHourData(Model model) {
        List<TicketCountByHour> ticketCountsByHour = chartsService.getTicketsCountByHour();

        //Amount of tickets bought for each Movie this Month
        List<Long> ticketCountsByHourAmount = ticketCountsByHour.stream()
                .map(TicketCountByHour::getTicketsCount)
                .toList();

        //Movies titles
        List<String> ticketCountsByHourTitles = ticketCountsByHour.stream()
                .map(ticketCountByHour -> ticketCountByHour.getHourOfDay() + ":00 - " + ticketCountByHour.getHourOfDay() + ":59")
                .toList();

        model.addAttribute("chart_ticketsCountsByHour", ticketCountsByHourAmount);//chart data
        model.addAttribute("chart_moviesTicketsThisMonthTitles", ticketCountsByHourTitles);//chart labels
    }

    private void addChartSessionsForDaysOfWeekData(Model model){
        List<SessionsByDayOfWeekProjection> sessionsForDaysOfWeek = chartsService.findSessionsForDaysOfWeek();

        //Amount of tickets bought for each Movie this Month
        List<Integer> sessionsForDaysOfWeekData = sessionsForDaysOfWeek.stream()
                .map(SessionsByDayOfWeekProjection::getSessionsCount)
                .toList();

        //Movies titles
        List<String> sessionsForDaysOfWeekTiles = sessionsForDaysOfWeek.stream()
                .map(SessionsByDayOfWeekProjection::getDayOfWeek)
                .toList();

        model.addAttribute("chart_sessionsForDaysOfWeekCount", sessionsForDaysOfWeekData);//chart data
        model.addAttribute("chart_sessionsForDaysOfWeekTitles", sessionsForDaysOfWeekTiles);//chart labels
    }

}

/**
 * The Date utils for counting DAYS between start and end dates.
 */
class DateUtils {
    /**
     * Days between start and end.
     *
     * @param start start date
     * @param end end date
     * @return long - amount of days between start and end dates
     */
    public static long daysBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.DAYS.between(start, end);
    }
}

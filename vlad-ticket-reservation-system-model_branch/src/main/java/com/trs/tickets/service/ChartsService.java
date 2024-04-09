package com.trs.tickets.service;

import com.trs.tickets.model.dto.projection.MovieSessionsProjection;
import com.trs.tickets.model.dto.projection.MoviesTicketsBoughtThisMonthProjection;
import com.trs.tickets.model.dto.projection.SessionsByDayOfWeekProjection;
import com.trs.tickets.model.dto.projection.TicketCountByHour;
import com.trs.tickets.repository.ChartsRepository;
import com.trs.tickets.repository.MovieRepository;
import com.trs.tickets.repository.SessionRepository;
import com.trs.tickets.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChartsService {
    private final TicketRepository ticketRepository;
    private final SessionRepository sessionRepository;
    private final MovieRepository movieRepository;

    private final ChartsRepository chartsRepository;


    public List<MovieSessionsProjection> getMoviesSessionsCount() {
        return chartsRepository.findMoviesAndSessionCounts();
    }

    public List<Integer> findInactiveSessionCounts(){
        return chartsRepository.findInactiveSessionCounts();
    }

    public List<MoviesTicketsBoughtThisMonthProjection> findTicketsBoughtForMoviesThisMonth() {
        final LocalDate now = LocalDate.now().plusDays(1);
        final LocalDate startOfMonth = now.withDayOfMonth(1);
        return chartsRepository.findTicketsBoughtForMoviesThisMonth(startOfMonth, now);
    }

    public List<TicketCountByHour> getTicketsCountByHour(){
        return chartsRepository.findTicketsCountByHour();
    }

    public List<SessionsByDayOfWeekProjection> findSessionsForDaysOfWeek(){
        final LocalDate now = LocalDate.now().plusDays(1);
        final LocalDate startOfMonth = now.withDayOfMonth(1);
        return chartsRepository.findSessionsForDaysOfWeek(startOfMonth, now);
    }































    //OLD
    // ^
    // |
    public Map<String, Integer> getFilmsTicketsData(){
        List<Map<String, Object>> results = ticketRepository.countTicketsByMovieTitle();
        Map<String, Integer> ticketCounts = new HashMap<>();
        for (Map<String, Object> result : results) {
            String title = (String) result.get("title");
            Long count = (Long) result.get("ticketCount");
            ticketCounts.put(title, count.intValue());
        }
        return ticketCounts;
    }


    public Map<String, Double> getTicketSalesByMovie() {
        List<Map<String, Object>> results = ticketRepository.sumTicketSalesByMovie();
        Map<String, Double> salesData = new HashMap<>();
        for (Map<String, Object> result : results) {
            String movieTitle = (String) result.get("title");
            BigDecimal totalSales = (BigDecimal) result.get("totalSales");
            salesData.put(movieTitle, totalSales != null ? totalSales.doubleValue() : 0.0);
        }
        return salesData;
    }


    public Map<String, Integer> getTicketCountsByTimeSlot() {
        List<Map<String, Object>> results = ticketRepository.countTicketsByTimeSlot();
        Map<String, Integer> ticketCounts = new LinkedHashMap<>(); // Using LinkedHashMap to maintain order
        for (Map<String, Object> result : results) {
            String timeSlot = (String) result.get("timeSlot");
            Long count = (Long) result.get("ticketCount");
            ticketCounts.put(timeSlot, count.intValue());
        }
        return ticketCounts;
    }

    public Map<String, BigDecimal> getTotalRevenueByMovie() {
        List<Map<String, Object>> results = ticketRepository.totalRevenueByMovie();
        Map<String, BigDecimal> revenueData = new HashMap<>();
        for (Map<String, Object> result : results) {
            String movieTitle = (String) result.get("movieTitle");
            BigDecimal totalRevenue = (BigDecimal) result.get("totalRevenue");
            revenueData.put(movieTitle, totalRevenue);
        }
        return revenueData;
    }

    public Map<String, Double> getHallUtilizationRates() {
        List<Map<String, Object>> results = sessionRepository.countSessionsByHall();
        Map<String, Double> utilizationRates = new HashMap<>();
        int maxSessionsPerDay = 5; // Hypothetical number
        int numberOfDays = 30;// Calculate the number of days in your data range

        for (Map<String, Object> result : results) {
            String hallName = (String) result.get("hallName");
            Long sessionCount = (Long) result.get("sessionCount");
            double utilizationRate = (double) sessionCount / (maxSessionsPerDay * numberOfDays) * 100;
            utilizationRates.put(hallName, utilizationRate);
        }
        return utilizationRates;
    }

    public Map<String, Long> getTicketSalesByDuration() {
        List<Map<String, Object>> results = ticketRepository.sumTicketSalesByDuration();
        Map<String, Long> salesByDuration = new HashMap<>();

        for (Map<String, Object> result : results) {
            String durationRange = (String) result.get("durationRange");
            BigDecimal totalSales = (BigDecimal) result.get("totalSales");

            // Convert BigDecimal to Long
            salesByDuration.put(durationRange, totalSales != null ? totalSales.longValue() : 0L);
        }

        return salesByDuration;
    }

    public Map<String, Long> getTicketSalesByDayOfWeek() {
        List<Map<String, Object>> results = ticketRepository.sumTicketSalesByDayOfWeek();
        Map<String, Long> salesByDayOfWeek = new HashMap<>();

        for (Map<String, Object> result : results) {
            String dayOfWeek = (String) result.get("dayOfWeek");
            BigDecimal totalSales = (BigDecimal) result.get("totalSales");

            // Convert BigDecimal to Long
            salesByDayOfWeek.put(dayOfWeek, totalSales != null ? totalSales.longValue() : 0L);
        }

        return salesByDayOfWeek;
    }


}

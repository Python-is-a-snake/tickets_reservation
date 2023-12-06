package com.trs.tickets.controller;

import com.trs.tickets.service.ChartsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class TestPlotsController {

    private final ChartsService chartsService;

    @GetMapping("/1")
    public String getStats(Model model) {
        Map<String, Integer> filmDataMap = chartsService.getFilmsTicketsData();
        List<List<Object>> chartData = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : filmDataMap.entrySet()) {
            List<Object> row = new ArrayList<>();
            row.add(entry.getKey());
            row.add(entry.getValue());
            chartData.add(row);
        }

        model.addAttribute("filmData", chartData);

        return "stats-page";
    }

    @GetMapping("/2")
    public String movieRevenue(Model model) {
        Map<String, BigDecimal> revenueData = chartsService.getTotalRevenueByMovie();
        model.addAttribute("revenueData", revenueData);
        return "stats-page2";
    }

    @GetMapping("/3")
    public String movieSales(Model model) {
        Map<String, Long> salesByDuration = chartsService.getTicketSalesByDuration();
        model.addAttribute("salesByDuration", salesByDuration);
        return "stats-page3";
    }

    @GetMapping("/4")
    public String hallUtilization(Model model) {
        Map<String, Double> utilizationData = chartsService.getHallUtilizationRates();
        model.addAttribute("utilizationData", utilizationData);
        return "stats-page4";
    }

    @GetMapping("/5")
    public String popularityByTimeSlot(Model model) {
        Map<String, Integer> timeSlotData = chartsService.getTicketCountsByTimeSlot();
        model.addAttribute("timeSlotData", timeSlotData);
        return "stats-page5";
    }
    @GetMapping("/6")
    public String ticketSalesByDayOfWeek(Model model) {
        Map<String, Long> salesByDayOfWeek = chartsService.getTicketSalesByDayOfWeek();
        model.addAttribute("salesByDayOfWeek", salesByDayOfWeek);
        return "stats-page6";
    }


}

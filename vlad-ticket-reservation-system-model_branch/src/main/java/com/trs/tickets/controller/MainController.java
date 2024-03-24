package com.trs.tickets.controller;

import com.trs.tickets.model.entity.Session;
import com.trs.tickets.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final SessionService sessionService;

    @GetMapping("/")
    public String redirectToMainPage() {
        return "redirect:/movies";
    }

    @GetMapping("/access-denied")
    public String accessDeniedPage() {
        return "error/access-denied-page";
    }

    @GetMapping("/contact-us")
    public String contactUsPage() {
        return "contact-us-page";
    }

    @GetMapping("/test")
    public String test(Model model){
        List<Session> allNotAvailableSessionsSortedByDateTime = sessionService.getAllNotAvailableSessionsSortedByDateTime();
        model.addAttribute("unavailableSessions", allNotAvailableSessionsSortedByDateTime);
        model.addAttribute("dateUtils", new DateUtils());
        return "/admin/test";
    }

}

class DateUtils {
    public static long daysBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.DAYS.between(start, end);
    }
}

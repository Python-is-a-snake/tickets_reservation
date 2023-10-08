package com.trs.tickets.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String redirectToMainPage() {
        return "redirect:/movies";
    }

    @GetMapping("/access-denied")
    public String accessDeniedPage() {
        return "access-denied-page";
    }

    @GetMapping("/contact-us")
    public String contactUsPage() {
        return "contact-us-page";
    }

    @GetMapping("/test")
    public String testPage() {
        return "admin-users";
    }
}

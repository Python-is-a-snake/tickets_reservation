package com.trs.tickets.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
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

    @GetMapping("/t")
    public String template(){
        return "template";
    }

    @GetMapping("/n")
    public String seats(){
        return "buy-ticket/new";
    }
}

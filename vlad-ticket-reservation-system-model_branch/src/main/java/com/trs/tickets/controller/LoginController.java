package com.trs.tickets.controller;

import com.trs.tickets.model.dto.UserCreateDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {


    @GetMapping("/login")
    public String loginPage(Model model, HttpServletRequest request) {

        return "login-page";
    }

    @PostMapping("/login")
    public String login() {
        return "redirect:/movies";
    }

    @GetMapping("/register")
    public String registrationPage(Model model) {
        UserCreateDto userCreateDto = new UserCreateDto();
        model.addAttribute("userCreateDto", userCreateDto);
        return "register-page";
    }

}

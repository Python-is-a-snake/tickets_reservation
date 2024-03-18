package com.trs.tickets.controller;

import com.trs.tickets.model.dto.UserCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "account/login-page";
    }

    @PostMapping("/login")
    public String login() {
        return "redirect:/movies";
    }

    @GetMapping("/register")
    public String registrationPage(Model model) {
        UserCreateDto userCreateDto = new UserCreateDto();
        model.addAttribute("userCreateDto", userCreateDto);
        return "account/register-page";
    }

}

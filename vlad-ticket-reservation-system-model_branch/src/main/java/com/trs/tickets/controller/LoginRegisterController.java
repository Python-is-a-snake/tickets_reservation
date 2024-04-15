package com.trs.tickets.controller;

import com.trs.tickets.model.dto.UserCreateDto;
import com.trs.tickets.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginRegisterController {
    private final UserService userService;

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

    @PostMapping("/doRegister")
    public String createUser(@Valid @ModelAttribute("userCreateDto") UserCreateDto userCreateDto, BindingResult bindingResult, Model model) {
        if (!userService.usernameIsUnique(userCreateDto.getUsername())) {
            bindingResult.addError(new FieldError("userCreateDto", "username", "This username is already in use!"));
        }
//        if (!userCreateDto.getUsername().isBlank() && !userService.isEmailCorrect(userCreateDto)) {//        Validate Email using Mailbox Validation
//            bindingResult.addError(new FieldError("userCreateDto", "username", "Incorrect Email!"));
//        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("userCreateDto", userCreateDto);
            model.addAttribute("bindingResult", bindingResult);
            return "account/register-page";
        }
        userCreateDto.setIsActive(true);

        userService.addUser(userCreateDto);
        return "redirect:/login";
    }

}

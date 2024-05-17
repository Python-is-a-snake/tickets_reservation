package com.trs.tickets.controller;

import com.trs.tickets.model.dto.UserCreateDto;
import com.trs.tickets.model.dto.UserDto;
import com.trs.tickets.model.entity.PasswordResetToken;
import com.trs.tickets.repository.TokenRepository;
import com.trs.tickets.service.MailService;
import com.trs.tickets.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginRegisterController {
    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final MailService mailService;

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

    @GetMapping("/forgotPassword")
    public String forgotPasswordPage() {
        return "account/forgot-password-page";
    }

    @PostMapping("/forgotPassword")
    public String restorePassword(@ModelAttribute UserDto userDto) {
        log.info("UserDto: {}", userDto);
        UserDto user = userService.findByUsername(userDto.getUsername());
        log.info("User to reset password found : {}", user);
        if(user != null){
            new Thread(() -> mailService.sendEmail(user)).start();
            return "redirect:/login";
        }
        return "redirect:/forgotPassword?error";
    }

    @GetMapping("/resetPassword/{token}")
    public String resetPasswordForm(Model model, @PathVariable String token) {
        PasswordResetToken reset = tokenRepository.findByToken(token);
        if (reset != null && !mailService.hasExpired(reset.getExpiryDateTime())) {
            model.addAttribute("email", reset.getUser().getUsername());
            return "account/reset-password-page";
        }

        return "redirect:/forgotPassword?error";
    }

    @PostMapping("/resetPassword")
    public String passwordResetProcess(@ModelAttribute UserCreateDto userDto) {
        log.info("UserCreateDto: {}", userDto);
        userService.resetPassword(userDto);
        return "redirect:/login";
    }

}

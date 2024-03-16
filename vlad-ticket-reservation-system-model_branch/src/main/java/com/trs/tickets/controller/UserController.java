package com.trs.tickets.controller;

import com.trs.tickets.configs.Role;
import com.trs.tickets.model.dto.UserCreateDto;
import com.trs.tickets.service.TicketService;
import com.trs.tickets.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final TicketService ticketService;

    //REGISTRATION
    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("userCreateDto") UserCreateDto userCreateDto, BindingResult bindingResult, Model model) {
        if (!userService.usernameIsUnique(userCreateDto.getUsername())) {
            bindingResult.addError(new FieldError("userCreateDto", "username", "This username is already in use!"));
        }

        //Validate Email using Mailbox Validation
        if (!userService.isEmailCorrect(userCreateDto)) {
            bindingResult.addError(new FieldError("userCreateDto", "username", "Incorrect Email!"));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("userCreateDto", userCreateDto);
            return "register-page";
        }

        userService.addUser(userCreateDto);
        return "redirect:/login";
    }

    //USER
    @GetMapping("/profile")
    public String profilePage(Model model, Authentication authentication) {
        model.addAttribute("user", userService.getUserByUsername(authentication.getName()).get(0));
        model.addAttribute("tickets", ticketService.findByUserName(authentication.getName()));
        return "profile-page";
    }

    //ADMIN

    //view all users page
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String allUsersPage(Model model, Authentication authentication) {
        model.addAttribute("users", userService.getAllUsersExcept(authentication.getName()));
        return "admin-users";
    }

    //search user by username
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String getUsersByUsername(@RequestParam(name = "username", required = false) String username, Model model, Authentication authentication) {
        model.addAttribute("users", userService.getUserByUsernameExcept(username, authentication.getName()));
        return "admin-users";
    }

    //make user an Admin
    @PostMapping("/set-admin/{id}")
    @PreAuthorize("hasAuthority('CEO')")
    public String setUserRoleAdmin(@PathVariable("id") Long id) {
        userService.changeUserRole(id, Role.ADMIN);
        return "redirect:/user/all";
    }

    //remove Admin role from user
    @PostMapping("/remove-admin/{id}")
    @PreAuthorize("hasAuthority('CEO')")
    public String removeUserRoleAdmin(@PathVariable("id") Long id) {
        userService.changeUserRole(id, Role.USER);
        return "redirect:/user/all";
    }

    //delete user
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/user/all";
    }
}

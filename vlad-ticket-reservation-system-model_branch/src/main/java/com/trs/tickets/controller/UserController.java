package com.trs.tickets.controller;

import com.trs.tickets.configs.Role;
import com.trs.tickets.model.dto.UserCreateDto;
import com.trs.tickets.model.dto.UserDto;
import com.trs.tickets.service.PageSizeCheckerService;
import com.trs.tickets.service.TicketService;
import com.trs.tickets.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    private final PageSizeCheckerService pageSizeCheckerService;

    //REGISTRATION
    @PostMapping("/create")
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

        userService.addUser(userCreateDto);
        return "redirect:/login";
    }

    //USER
    @GetMapping("/profile")
    public String profilePage(Model model, Authentication authentication) {
        model.addAttribute("user", userService.getUserByUsername(authentication.getName()).get(0));
        model.addAttribute("tickets", ticketService.findByUserName(authentication.getName()));
        return "account/profile-page";
    }

    //ADMIN

    //view all users page
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String allUsersPage(Model model,
                               Authentication authentication,
                               @RequestParam(name = "page", defaultValue = "0") Integer page,
                               @RequestParam(name = "size", defaultValue = "6") Integer size) {

        page = pageSizeCheckerService.checkPage(page);
        size = pageSizeCheckerService.checkSize(size);

        Page<UserDto> allUsersExcept = userService.getAllUsersExcept(authentication.getName(), page, size);

        model.addAttribute("users",allUsersExcept.getContent());
        model.addAttribute("currentPage", allUsersExcept.getNumber());
        model.addAttribute("totalItems", allUsersExcept.getTotalElements());
        model.addAttribute("totalPages", allUsersExcept.getTotalPages());
        model.addAttribute("pageSize", size);
        return "admin/admin-users";
    }

    //search user by username
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CEO')")
    public String getUsersByUsername(Model model,
                                     Authentication authentication,
                                     @RequestParam(name = "username", required = false) String username,
                                     @RequestParam(name = "page", defaultValue = "0") Integer page,
                                     @RequestParam(name = "size", defaultValue = "6") Integer size
                                     ) {

        page = pageSizeCheckerService.checkPage(page);
        size = pageSizeCheckerService.checkSize(size);
        Page<UserDto> usersByUsernameExcept = userService.getUserByUsernameExcept(username, authentication.getName(), page, size);

        model.addAttribute("users", usersByUsernameExcept);
        model.addAttribute("movies", usersByUsernameExcept.getContent());
        model.addAttribute("currentPage", usersByUsernameExcept.getNumber());
        model.addAttribute("totalItems", usersByUsernameExcept.getTotalElements());
        model.addAttribute("totalPages", usersByUsernameExcept.getTotalPages());
        model.addAttribute("pageSize", size);

        return "admin/admin-users";
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

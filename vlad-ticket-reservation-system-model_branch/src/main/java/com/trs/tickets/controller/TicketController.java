package com.trs.tickets.controller;

import com.trs.tickets.model.dto.TicketDto;
import com.trs.tickets.model.dto.TicketPurchaseRequest;
import com.trs.tickets.model.dto.UserDto;
import com.trs.tickets.service.PlaceService;
import com.trs.tickets.service.TicketService;
import com.trs.tickets.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final UserService userService;
    private final PlaceService placeService;

    //reserve ticket and go to Profile page
    @PostMapping("/buy")
    public String purchaseTicket(@ModelAttribute("ticket") TicketDto ticketDto) {

        ticketService.addTicket(ticketDto, ticketDto.getPlaceId(), ticketDto.getSessionId(), ticketDto.getUserId());

        return "redirect:/user/profile";
    }

    @PostMapping("/do_purchase")
    public String purchaseTickets(@RequestBody TicketPurchaseRequest request, Authentication authentication) {
        // Check that the user is allowed to buy tickets
        String username = authentication.getName();
        System.out.println("USERNAME: " + username);
        UserDto user = userService.getUserByUsername(username);

        // You might want to validate the request here (e.g., check that the seats are available)

        // Create a ticket for each selected seat
        for (Long placeId : request.getPlaceIds()) {
            TicketDto ticketDto = new TicketDto();
            // Assuming TicketPurchaseRequest contains a list of placeIds and a sessionId
            ticketDto.setSessionId(request.getSessionId());
            ticketDto.setPlaceId(placeId);
            ticketDto.setUserId(user.getId());
            ticketDto.setPrice(placeService.getPlaceById(placeId).getPlaceType().getPrice()); // Example, adjust as necessary

            ticketService.addTicket(ticketDto, placeId, request.getSessionId(), user.getId());
        }

        return "redirect:/user/profile";
    }

}

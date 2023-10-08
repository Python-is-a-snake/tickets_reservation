package com.trs.tickets.controller;

import com.trs.tickets.model.dto.TicketDto;
import com.trs.tickets.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping("/buy")
    public String purchaseTicket(@ModelAttribute("ticket") TicketDto ticketDto, Model model) {

        ticketService.addTicket(ticketDto, ticketDto.getPlaceId(), ticketDto.getSessionId(), ticketDto.getUserId());

        return "redirect:/user/profile";
    }
}

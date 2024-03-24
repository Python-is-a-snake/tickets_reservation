package com.trs.tickets.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.trs.tickets.model.dto.TicketDto;
import com.trs.tickets.service.PayPalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/paypal")
public class PayPalController {
    private final PayPalService payPalService;

//    private final Logger log;
private final String HOST_LOCAL = "http://localhost:5550";

    @PostMapping("/payment/create")
    public RedirectView createPayment(Model model, @ModelAttribute("ticket") TicketDto ticketDto) {
        try {
            //TODO create Payment DTO
            log.info("Trying to create payment");
            Payment payment = payPalService.createPayment(ticketDto.getPrice(), "USD", "Paypal", "sale", "ticket reserve test",
                    HOST_LOCAL + "/paypal/payment/success", HOST_LOCAL + "/paypal/payment/cancel");

            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    log.info("Redirect to PayPal API");

                    return new RedirectView(links.getHref());
                }
            }

        } catch (PayPalRESTException e) {
            log.error("Payment error happened");
            throw new RuntimeException(e);
        }

        //if payment not succeed
        log.info("Payment not succeed! Redirect to error page");
        return new RedirectView("/payment/error");
    }


    @GetMapping("/payment/cancel")
    public String paymentCancel() {
        return "redirect:/sessions/buy-tickets/24";
    }

    @GetMapping("/payment/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) {
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "paymentSuccess";
            }
        } catch (PayPalRESTException e) {
//            log.error("Error occurred:: ", e);
        }
        return "paymentSuccess";

    }
}

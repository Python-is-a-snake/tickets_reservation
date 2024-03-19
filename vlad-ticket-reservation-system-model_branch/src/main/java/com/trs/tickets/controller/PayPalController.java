package com.trs.tickets.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.trs.tickets.service.PayPalService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/paypal")
@Slf4j
public class PayPalController {
    private final PayPalService payPalService;
//    private final Logger log;

    @PostMapping("/payment/create")
    public RedirectView createPayment(Model model) {
        try {
            //TODO create Payment DTO
            Payment payment = payPalService.createPayment(10.0, "USD", "Paypal", "sale", "ticket reserve test",
                    "/paypal/payment/success", "/paypal/payment/cancel");

            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return new RedirectView(links.getHref());
                }
            }

        } catch (PayPalRESTException e) {
//            log.warning();
            throw new RuntimeException(e);
        }

        return new RedirectView("/");
    }


    @GetMapping("/payment/cancel")
    public String paymentCancel() {
        return "ALL BAD";
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

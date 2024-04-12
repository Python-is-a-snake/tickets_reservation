package com.trs.tickets.model.dto;

import lombok.*;
import java.util.List;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TicketPurchaseRequest {
    private Long sessionId;
    private List<Long> placeIds;
}

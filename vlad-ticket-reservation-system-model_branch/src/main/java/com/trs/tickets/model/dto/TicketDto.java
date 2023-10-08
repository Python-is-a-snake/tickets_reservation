package com.trs.tickets.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TicketDto extends BaseDto {
    private Long userId;
    private Long placeId;
    private Long sessionId;
    private BigDecimal price;
    private LocalDateTime purchaseDate;
    //private String ticketCode;

}

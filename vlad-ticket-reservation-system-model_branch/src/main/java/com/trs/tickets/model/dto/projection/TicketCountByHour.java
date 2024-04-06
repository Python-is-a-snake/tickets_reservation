package com.trs.tickets.model.dto.projection;

public interface TicketCountByHour {
    Integer getHourOfDay();
    Long getTicketsCount();
}
package com.trs.tickets.model.dto.projection;

public interface SessionsByDayOfWeekProjection {
    String getDayOfWeek();

    Integer getSessionsCount();
}

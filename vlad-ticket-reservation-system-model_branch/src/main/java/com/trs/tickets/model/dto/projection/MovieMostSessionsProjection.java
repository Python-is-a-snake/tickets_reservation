package com.trs.tickets.model.dto.projection;

/**
 * The interface Movie sessions projection,
 * used for retrieving data for Movie Sessions Count chart.
 */
public interface MovieMostSessionsProjection {

    Long getId();
    /**
     * Gets title.
     *
     * @return the title
     */
    String getTitle();

    /**
     * Gets session count.
     *
     * @return the session count
     */
    Integer getSessionCount();
}
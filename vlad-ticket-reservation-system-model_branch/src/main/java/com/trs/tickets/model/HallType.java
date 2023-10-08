package com.trs.tickets.model;

/**
 * Type of the Hall.
 */
public enum HallType {

    /**
     * IMAX Hall
     */
    HALL_IMAX,

    /**
     * 3D Hall
     */
    HALL_3D,

    /**
     * 2D Hall
     */
    HALL_2D;


    @Override
    public String toString() {
        return name().substring(5);
    }
}

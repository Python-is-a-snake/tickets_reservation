package com.trs.tickets.model;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * Type of the Place in the Hall.
 * Determines the price of the Place.
 */
public enum PlaceType {
    /**
     * The most expensive place in the Hall
     */
    VIP(BigDecimal.valueOf(200)),

    /**
     * Normal place in the Hall
     */
    NORMAL(BigDecimal.valueOf(100)),

    /**
     * The cheapest place in the Hall
     */
    CHEAP(BigDecimal.valueOf(50));

    @Getter
    private final BigDecimal price;

    PlaceType(BigDecimal price) {
        this.price = price;
    }
}

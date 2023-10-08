package com.trs.tickets.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieDtoTest {

    @Test
    void getTrailerCode() {
        MovieDto dto = new MovieDto();
        dto.setTrailerUrl("https://www.youtube.com/watch?v=lB95KLmpLR4");

        assertEquals("lB95KLmpLR4", dto.getTrailerCode());
    }
}

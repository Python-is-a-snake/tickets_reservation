package com.trs.tickets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trs.tickets.model.entity.Hall;
import com.trs.tickets.service.HallService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(HallController.class)
class HallControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    HallService hallService;

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<Long> idCaptor;

    @Captor
    ArgumentCaptor<Hall> hallCaptor;
    private Hall testHall;

    @BeforeEach
    void setUp() {
        testHall = new Hall();
        testHall.setId(1L);
        testHall.setName("Test Hall");

    }

    @Test
    void testGetAllHalls_returnsListOfHalls() throws Exception {
        Hall hall2 = new Hall();

        given(hallService.getAllHalls()).willReturn(List.of(testHall, hall2));

        mockMvc.perform(get("/api/hall")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)));

        verify(hallService).getAllHalls();
    }

    @Test
    void testGetHallById_returnsHall() throws Exception {
        given(hallService.getHallById(anyLong())).willReturn(testHall);

        mockMvc.perform(get("/api/hall/" + testHall.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(testHall.getName())));

        verify(hallService).getHallById(idCaptor.capture());
        assertEquals(testHall.getId(), idCaptor.getValue());
    }

    @Test
    void testUpdateHall_returnsUpdatedHall() throws Exception {
        given(hallService.updateHall(any(Hall.class))).willReturn(testHall);

        testHall.setName("New name");

        mockMvc.perform(put("/api/hall")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testHall)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(testHall.getName())));

        verify(hallService).updateHall(hallCaptor.capture());
        assertEquals(testHall, hallCaptor.getValue());
    }

    @Test
    void testAddHall_returnsAddedHall() throws Exception {
        given(hallService.addHall(any(Hall.class))).willReturn(testHall);

        testHall.setName("New name");

        mockMvc.perform(post("/api/hall")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testHall)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(testHall.getName())));

        verify(hallService).addHall(hallCaptor.capture());

        assertEquals(testHall, hallCaptor.getValue());

    }

    @Test
    void testDeleteHall() throws Exception {
        mockMvc.perform(delete("/api/hall/" + testHall.getId())
                .accept(MediaType.APPLICATION_JSON));

        verify(hallService).deleteHall(idCaptor.capture());
        assertEquals(testHall.getId(), idCaptor.getValue());
    }

}
//package com.trs.tickets.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.trs.tickets.model.entity.Cinema;
//import com.trs.tickets.service.CinemaService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.hamcrest.core.Is.is;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.verify;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
////@WebMvcTest(CinemaController.class)
//class CinemaControllerTest {
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @MockBean
//    private CinemaService cinemaService;
//
//    @Captor
//    ArgumentCaptor<Long> idCaptor;
//
//    @Captor
//    ArgumentCaptor<Cinema> cinemaCaptor;
//    private Cinema testCinema;
//
//    @BeforeEach
//    void setUp() {
//        testCinema = new Cinema();
//        testCinema.setId(1L);
//        testCinema.setName("Cinema Name");
//        testCinema.setAddress("Cinema Address");
//        testCinema.setEmail("Cinema email");
//        testCinema.setPhone("111111111111");
//    }
//
//    @Test
//    void testGetCinemaById_returnsCinema() throws Exception {
//        given(cinemaService.getCinemaById(anyLong())).willReturn(testCinema);
//
//        mockMvc.perform(get("/api/cinema/" + testCinema.getId())
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.name", is(testCinema.getName())));
//
//        verify(cinemaService).getCinemaById(idCaptor.capture());
//
//        assertEquals(testCinema.getId(), idCaptor.getValue());
//    }
//
//    @Test
//    void testGetAllCinemas_returnsAllCinemas() throws Exception {
//        Cinema cinema2 = new Cinema();
//        cinema2.setEmail("C 2 phone");
//        cinema2.setName("C 2 name");
//
//        given(cinemaService.getAllCinemas()).willReturn(new ArrayList<>(List.of(testCinema, cinema2)));
//
//        mockMvc.perform(get("/api/cinema")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.length()", is(2)));
//    }
//
//    @Test
//    void testAddCinema_returnsAddedCinema() throws Exception {
//        given(cinemaService.addCinema(any(Cinema.class))).willReturn(testCinema);
//
//        mockMvc.perform(post("/api/cinema")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(testCinema)))
//                .andExpect(jsonPath("$.name", is(testCinema.getName())));
//
//        verify(cinemaService).addCinema(cinemaCaptor.capture());
//        assertEquals(testCinema, cinemaCaptor.getValue());
//    }
//
//    @Test
//    void testUpdateCinema_returnsUpdatedCinema() throws Exception {
//        testCinema.setName("New Name");
//        given(cinemaService.updateCinema(any(Cinema.class))).willReturn(testCinema);
//
//        mockMvc.perform(put("/api/cinema")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(testCinema)))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.name", is(testCinema.getName())));
//
//        verify(cinemaService).updateCinema(cinemaCaptor.capture());
//
//        assertEquals(testCinema, cinemaCaptor.getValue());
//    }
//
//    @Test
//    void testDeleteCinema() throws Exception {
//        mockMvc.perform(delete("/api/cinema/"+testCinema.getId())
//                .accept(MediaType.APPLICATION_JSON));
//
//        verify(cinemaService).deleteCinema(idCaptor.capture());
//        assertEquals(testCinema.getId(), idCaptor.getValue());
//    }
//
//}

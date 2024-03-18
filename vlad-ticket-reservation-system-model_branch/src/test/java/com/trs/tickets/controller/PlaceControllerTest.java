//package com.trs.tickets.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.trs.tickets.model.entity.Place;
//import com.trs.tickets.service.PlaceService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import java.util.List;
//
//import static org.hamcrest.core.Is.is;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.verify;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
////@WebMvcTest(PlaceController.class)
//class PlaceControllerTest {
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    PlaceService placeService;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Captor
//    ArgumentCaptor<Long> idCaptor;
//
//    @Captor
//    ArgumentCaptor<Place> placeCaptor;
//    private Place testPlace;
//
//    @BeforeEach
//    void setUp() {
//        testPlace = new Place();
//        testPlace.setId(1L);
//        //testPlace.setPlaceCode("Place code");
//
//    }
//
//    @Test
//    void testGetAllPlaces_returnsListOfPlaces() throws Exception {
//        Place place2 = new Place();
//
//        given(placeService.getAllPlaces()).willReturn(List.of(testPlace, place2));
//
//        mockMvc.perform(get("/api/place")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.length()", is(2)));
//
//        verify(placeService).getAllPlaces();
//    }
//
//    @Test
//    void testGetPlaceById_returnsPlace() throws Exception {
//        given(placeService.getPlaceById(anyLong())).willReturn(testPlace);
//
//        mockMvc.perform(get("/api/place/" + testPlace.getId())
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//                //.andExpect(jsonPath("$.placeCode", is(testPlace.getPlaceCode())));
//
//        verify(placeService).getPlaceById(idCaptor.capture());
//        assertEquals(testPlace.getId(), idCaptor.getValue());
//    }
//
//    @Test
//    void testUpdatePlace_returnsUpdatedPlace() throws Exception {
//        given(placeService.updatePlace(any(Place.class))).willReturn(testPlace);
//
//
//        mockMvc.perform(put("/api/place")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(testPlace)))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//                //.andExpect(jsonPath("$.placeCode", is(testPlace.getPlaceCode())));
//
//        verify(placeService).updatePlace(placeCaptor.capture());
//        assertEquals(testPlace, placeCaptor.getValue());
//    }
//
//    @Test
//    void testAddPlace_returnsAddedPlace() throws Exception {
//        given(placeService.addPlace(any(Place.class))).willReturn(testPlace);
//
//
//        mockMvc.perform(post("/api/place")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(testPlace)))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
////                .andExpect(jsonPath("$.placeCode", is(testPlace.getPlaceCode())));
//        verify(placeService).addPlace(placeCaptor.capture());
//
//        assertEquals(testPlace, placeCaptor.getValue());
//
//    }
//
//    @Test
//    void testDeletePlace() throws Exception {
//        mockMvc.perform(delete("/api/place/" + testPlace.getId())
//                .accept(MediaType.APPLICATION_JSON));
//
//        verify(placeService).deletePlace(idCaptor.capture());
//        assertEquals(testPlace.getId(), idCaptor.getValue());
//    }
//
//
//}

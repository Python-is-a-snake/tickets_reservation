//package com.trs.tickets.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.trs.tickets.model.entity.Ticket;
//import com.trs.tickets.service.TicketService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.hamcrest.core.Is.is;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
////@WebMvcTest(TicketController.class)
//class TicketControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private TicketService ticketService;
//
//    @Captor
//    ArgumentCaptor<Long> idCaptor;
//
//    @Captor
//    ArgumentCaptor<Ticket> ticketCaptor;
//    private Ticket testTicket;
//
//    @BeforeEach
//    void setUp() {
//        testTicket = new Ticket();
//        testTicket.setId(1L);
//        testTicket.setPrice(BigDecimal.valueOf(20.0));
//    }
//
//    @Test
//    void testGetAllTickets_returnsListOfTickets() throws Exception {
//        Ticket ticket2 = new Ticket();
//        ticket2.setId(2L);
//        ticket2.setPrice(BigDecimal.valueOf(30.0));
//
//        given(ticketService.getAllTickets()).willReturn(List.of(testTicket, ticket2));
//
//        mockMvc.perform(get("/api/ticket")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.length()", is(2)));
//
//        verify(ticketService).getAllTickets();
//    }
//
//    @Test
//    void testGetTicketById_returnsTicket() throws Exception {
//
//        given(ticketService.getTicketById(anyLong())).willReturn(testTicket);
//
//        mockMvc.perform(get("/api/ticket/" + testTicket.getId())
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.price", is(testTicket.getPrice().doubleValue())));
//
//        verify(ticketService).getTicketById(idCaptor.capture());
//        assertEquals(testTicket.getId(), idCaptor.getValue());
//    }
//
//    @Test
//    void testUpdateTicket_returnsUpdatedTicket() throws Exception {
//        given(ticketService.updateTicket(any(Ticket.class))).willReturn(testTicket);
//
//        mockMvc.perform(put("/api/ticket")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testTicket)))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.price", is(testTicket.getPrice().doubleValue())));
//
//        verify(ticketService).updateTicket(ticketCaptor.capture());
//        assertEquals(testTicket, ticketCaptor.getValue());
//    }
//
//    @Test
//    void testAddTicket_returnsAddedTicket() throws Exception {
//
//       // given(ticketService.addTicket(any(Ticket.class))).willReturn(testTicket);
//
//        mockMvc.perform(post("/api/ticket")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testTicket)))
//           //     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//         //       .andExpect(jsonPath("$.price", is(testTicket.getPrice().doubleValue())));
//
//        //verify(ticketService).addTicket(ticketCaptor.capture());
//        assertEquals(testTicket, ticketCaptor.getValue());
//    }
//
//    @Test
//    void testDeleteTicket() throws Exception {
//        mockMvc.perform(delete("/api/ticket/" + testTicket.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON));
//
//        verify(ticketService).deleteTicket(idCaptor.capture());
//        assertEquals(testTicket.getId(), idCaptor.getValue());
//    }
//}

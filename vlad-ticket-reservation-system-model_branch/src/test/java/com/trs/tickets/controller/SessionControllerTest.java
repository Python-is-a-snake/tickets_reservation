//package com.trs.tickets.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.trs.tickets.model.entity.Session;
//import com.trs.tickets.service.SessionService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Answers;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.AdditionalAnswers.returnsFirstArg;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//
//@WebMvcTest(SessionController.class)
//class SessionControllerTest {
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    SessionService sessionService;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Captor
//    ArgumentCaptor<Long> idCaptor;
//
//    @Captor
//    ArgumentCaptor<Session> sessionCaptor;
//
//    private Session testSession;
//
//    @BeforeEach
//    void setUp() {
//        testSession = new Session();
//        testSession.setId(1L);
////        testSession = mock(Session.class);
////        when(testSession.getId()).thenReturn(1L);
//    }
//
//    @Test
//    void testGetSessionById_returnsSession() throws Exception {
//       // given(sessionService.getSessionById(anyLong())).willReturn(testSession);
//
//        mockMvc.perform(get("/api/session/" + testSession.getId())
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//
//        verify(sessionService).getSessionById(idCaptor.capture());
//        assertEquals(testSession.getId(), idCaptor.getValue());
//    }
//
//    @Test
//    void testGetAllSessions_returnsListOfSessions() throws Exception {
//        Session session2 = new Session();
//        given(sessionService.getAllSessions()).willReturn(List.of(testSession, session2));
//
//        mockMvc.perform(get("/api/session")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//
//        verify(sessionService).getAllSessions();
//    }
//
//    @Test
//        //TODO Fails
//    void testAddSession_returnsAddedSession() throws Exception {
//        given(sessionService.addSession(testSession)).willReturn(testSession);
//
//        mockMvc.perform(post("/api/session")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testSession)))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//
//        verify(sessionService).addSession(sessionCaptor.capture());
//        assertEquals(testSession, sessionCaptor.getValue());
//    }
//
//    @Test
//    void testUpdateSession_returnsUpdatedSession() throws Exception {
//        //given(sessionService.updateSession(any(Session.class))).willReturn(testSession);
//
//        mockMvc.perform(put("/api/session")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testSession)))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//
//        //verify(sessionService).updateSession(sessionCaptor.capture());
//        assertEquals(testSession, sessionCaptor.getValue());
//    }
//
//    @Test
//    void testDeleteSession() throws Exception {
//        mockMvc.perform(delete("/api/session/" + testSession.getId())
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        verify(sessionService).deleteSession(idCaptor.capture());
//        assertEquals(testSession.getId(), idCaptor.getValue());
//    }
//}

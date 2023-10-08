//package com.trs.tickets.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.trs.tickets.model.entity.User;
//import com.trs.tickets.service.UserService;
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
//@WebMvcTest(UserController.class)
//class UserControllerTest {
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    UserService userService;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Captor
//    ArgumentCaptor<Long> idCaptor;
//
//    @Captor
//    ArgumentCaptor<User> userCaptor;
//
//    private User testUser;
//
//    @BeforeEach
//    void setUp() {
//        testUser = new User();
//        testUser.setId(1L);
//        testUser.setName("Test User");
//        testUser.setEmail("Email");
//    }
//
//    @Test
//    void testGetUserById_returnsUser() throws Exception {
//        given(userService.getUserById(anyLong())).willReturn(testUser);
//
//        mockMvc.perform(get("/api/user/" + testUser.getId())
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.name", is(testUser.getName())));
//
//        verify(userService).getUserById(idCaptor.capture());
//        assertEquals(testUser.getId(), idCaptor.getValue());
//    }
//
//    @Test
//    void testGetAllUsers_returnsListOfUsers() throws Exception {
//        User user2 = new User();
//        given(userService.getAllUsers()).willReturn(List.of(testUser, user2));
//
//        mockMvc.perform(get("/api/user")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.length()", is(2)));
//
//        verify(userService).getAllUsers();
//    }
//
//    @Test
//    void testAddUser_returnsAddedUser() throws Exception {
//        given(userService.addUser(any(User.class))).willReturn(testUser);
//
//        mockMvc.perform(post("/api/user")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testUser)))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.name", is(testUser.getName())));
//
//        verify(userService).addUser(userCaptor.capture());
//        assertEquals(testUser, userCaptor.getValue());
//    }
//
//    @Test
//    void testUpdateUser_returnsUpdatedUser() throws Exception {
//        given(userService.updateUser(any(User.class))).willReturn(testUser);
//
//        testUser.setName("New Name");
//
//        mockMvc.perform(put("/api/user")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testUser)))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.name", is(testUser.getName())));
//
//        verify(userService).updateUser(userCaptor.capture());
//        assertEquals(testUser, userCaptor.getValue());
//    }
//
//    @Test
//    void testDeleteUser() throws Exception {
//        mockMvc.perform(delete("/api/user/" + testUser.getId()));
//
//        verify(userService).deleteUser(idCaptor.capture());
//        assertEquals(testUser.getId(), idCaptor.getValue());
//    }
//}
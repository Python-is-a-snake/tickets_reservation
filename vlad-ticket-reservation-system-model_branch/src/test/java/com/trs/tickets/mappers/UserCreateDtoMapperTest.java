package com.trs.tickets.mappers;

import com.trs.tickets.configs.Role;
import com.trs.tickets.model.dto.UserCreateDto;
import com.trs.tickets.model.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.*;

class UserCreateDtoMapperTest extends AbstractMapperTest {
    @Autowired
    private UserCreateDtoMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void convertDtoToEntity() {
        // given
        UserCreateDto userCreateDto = Mockito.mock(UserCreateDto.class);
        Mockito.when(userCreateDto.getUsername()).thenReturn("user@mail.com");
        Mockito.when(userCreateDto.getPassword()).thenReturn("password");
        //Mockito.when(userCreateDto.getRole()).thenReturn(Role.USER);

        // when
        User user = mapper.convert(userCreateDto);

        // then
        assertEquals(userCreateDto.getUsername(), user.getUsername());
        assertTrue(passwordEncoder.matches(userCreateDto.getPassword(), user.getPassword()));
       // assertEquals(userCreateDto.getRole(), user.getRole());
    }
}

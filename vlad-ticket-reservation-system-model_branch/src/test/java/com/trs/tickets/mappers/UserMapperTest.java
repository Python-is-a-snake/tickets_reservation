package com.trs.tickets.mappers;

import com.trs.tickets.configs.Role;
import com.trs.tickets.model.dto.UserDto;
import com.trs.tickets.model.entity.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest extends AbstractMapperTest {
    @Autowired
    private UserMapper mapper;

    @Nested
    class EntityToDtoTest {
        @Test
        void convertEntityToDto() {
            // given
            User user = Mockito.mock(User.class);
            Mockito.when(user.getId()).thenReturn(1L);
            Mockito.when(user.getUsername()).thenReturn("user@mail.com");
            Mockito.when(user.getPassword()).thenReturn("password");
            Mockito.when(user.getRole()).thenReturn(Role.USER);
            //Mockito.when(user.getRegistrationDate()).thenReturn(LocalDateTime.now());

            // when
            UserDto userDto = mapper.convert(user);

            // then
            assertEquals(user.getId(), userDto.getId());
            assertEquals(user.getUsername(), userDto.getUsername());
            assertEquals(user.getRole(), userDto.getRole());
            //assertEquals(user.getRegistrationDate(), userDto.getRegistrationDate());
        }
    }

    @Nested
    class DtoToEntityTest {
        @Test
        void convertDtoToEntity() {
            // given
            UserDto userDto = Mockito.mock(UserDto.class);
            Mockito.when(userDto.getId()).thenReturn(1L);
            Mockito.when(userDto.getUsername()).thenReturn("user@mail.com");
            Mockito.when(userDto.getRole()).thenReturn(Role.USER);
            //Mockito.when(userDto.getRegistrationDate()).thenReturn(LocalDateTime.now());

            // when
            User user = mapper.convert(userDto);

            // then
            assertEquals(userDto.getId(), user.getId());
            assertEquals(userDto.getUsername(), user.getUsername());
            assertEquals(userDto.getRole(), user.getRole());
            assertEquals(userDto.getRegistrationDate(), user.getRegistrationDate());
            assertNull(user.getPassword());
        }
    }
}

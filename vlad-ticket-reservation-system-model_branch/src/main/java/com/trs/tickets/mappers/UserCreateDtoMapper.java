package com.trs.tickets.mappers;

import com.trs.tickets.configs.Role;
import com.trs.tickets.model.dto.UserCreateDto;
import com.trs.tickets.model.entity.User;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserCreateDtoMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User convert(UserCreateDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);

        return user;
    }
}

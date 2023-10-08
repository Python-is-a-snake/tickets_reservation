package com.trs.tickets.mappers;

import com.trs.tickets.model.dto.UserDto;
import com.trs.tickets.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends GenericMapper<User, UserDto> {}

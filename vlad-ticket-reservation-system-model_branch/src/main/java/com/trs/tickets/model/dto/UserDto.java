package com.trs.tickets.model.dto;

import com.trs.tickets.configs.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserDto extends BaseDto {
    String username;
    Role role;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate registrationDate;
}

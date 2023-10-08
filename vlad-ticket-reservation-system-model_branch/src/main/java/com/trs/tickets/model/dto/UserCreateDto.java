package com.trs.tickets.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
    @Email
    String username;

    @NotBlank(message = "Can not be blank")
    @Length(min = 6, max = 120, message = "Too short password, should be more than 6 characters")
    String password;
}

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
    @NotBlank(message = "Can not be blank")
    @Length(min = 6, max = 120, message = "Too short email, should be more than 3 characters")
    String username;

    @NotBlank(message = "Can not be blank")
    @Length(min = 3, max = 120, message = "Too short password, should be more than 6 characters")
    String password;

    private Boolean isActive = true;
}

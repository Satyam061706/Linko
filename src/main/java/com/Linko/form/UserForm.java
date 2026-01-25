package com.Linko.form;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {

    @NotBlank(message = "Name is required")
    @Size(min = 3, message = "Min 3 character Required")
    private String name;

    @Email(message = "Invalid Email Address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Minimum 6 character is required")
    private String password;
    
    private String about;

    @Size(min = 8,max = 15,message = "Invalid Phone Number")
    @NotBlank(message = "PhoneNumber is required")
    private String phoneNumber;
}

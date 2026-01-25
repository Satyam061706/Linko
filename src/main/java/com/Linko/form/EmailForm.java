package com.Linko.form;

import org.springframework.web.multipart.MultipartFile;

import com.Linko.validators.ValidFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailForm {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email Address [ example@gmail.com ]")
    private String from;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email Address [ example@gmail.com ]")
    private String to;

    @NotBlank(message = "Subject is required")
    private String subject;

    private String content;

    @ValidFile(message = "Invalid File")
    private MultipartFile sendingImage;

    private String picture;

}

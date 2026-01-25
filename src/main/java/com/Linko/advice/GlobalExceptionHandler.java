package com.Linko.advice;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.Linko.exception.EmailAlreadyExistsException;
import com.Linko.form.UserForm;
import com.Linko.util.MessageType;
import com.Linko.util.message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public String handleEmailExists(
            EmailAlreadyExistsException ex,
            Model model, HttpSession session) {
        session.setAttribute("message", message.builder()
                .type(MessageType.red)
                .content("" + ex.getMessage())
                .build());
        model.addAttribute("userForm", new UserForm());
        return "registor";
    }

    // @ExceptionHandler(EmailAlreadyExistsException.class)
    // public String handleEmailExists(
    // EmailAlreadyExistsException ex,
    // Model model, HttpSession session) {
    // session.setAttribute("message", message.builder()
    // .type(MessageType.red)
    // .content("" + ex.getMessage())
    // .build());
    // model.addAttribute("userForm", new UserForm());
    // return "registor";
    // }
}

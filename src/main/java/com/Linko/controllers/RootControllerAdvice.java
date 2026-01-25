package com.Linko.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
// import org.springframework.security.core.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.Linko.entities.User;
import com.Linko.services.UserService;
import com.Linko.util.Helper;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class RootControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(RootControllerAdvice.class);
    private final UserService userService;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {

        if (authentication == null) {
            return;
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("loggedin username: {}", username);

        // fetching user form user service
        User user = userService.getUserByEmail(username);
        System.out.println(user);
        model.addAttribute("loggedInUser", user);
    }
}

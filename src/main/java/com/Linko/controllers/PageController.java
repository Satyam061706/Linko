package com.Linko.controllers;

import com.Linko.entities.User;
import com.Linko.form.UserForm;
import com.Linko.services.UserService;
import com.Linko.util.MessageType;
import com.Linko.util.message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final UserService userService;

    @RequestMapping("/")
    public String index() {
        return "home";
    }

    @RequestMapping(path = "/home")
    public String Home() {
        return "home";
    }

    @RequestMapping(path = "/about")
    public String about() {
        return "about";
    }

    @RequestMapping(path = "/services")
    public String services() {
        return "services";
    }

    @RequestMapping(path = "/contact")
    public String contact() {
        return "contact";
    }

    @RequestMapping(path = "/signup")
    public String signup() {
        return "signup";
    }

    // profile page
    @RequestMapping(path = "/registor")
    public String registor(Model model) {

        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        return "registor";
    }

    // login page
    @RequestMapping(path = "/login")
    public String login() {
        return "login";
    }

    // processing register
    @RequestMapping(path = "/do-register", method = RequestMethod.POST)
    public String processRegister(@ModelAttribute @Valid UserForm userForm, BindingResult bindingResult,
            HttpSession session) {
        // fetch form data
        System.out.println("processing register form");
        System.out.println(userForm);

        // validate form data
        if (bindingResult.hasErrors()) {
            return "registor";
        }

        // userForm to User

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePicture("https://www.istockphoto.com/photos/generic-user-avatar");
        user.setEnabled(false);

        // save to database
        User savedUser = userService.createUser(user);
        System.out.println(savedUser);

        message m = message.builder().content("Registration Successfully").type(MessageType.green).build();
        session.setAttribute("message", m);

        return "redirect:/registor";
    }
}

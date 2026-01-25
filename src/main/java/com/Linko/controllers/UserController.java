package com.Linko.controllers;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.Linko.entities.Contact;
import com.Linko.entities.User;
import com.Linko.form.ContactForm;
import com.Linko.form.ContactSearchForm;
import com.Linko.services.ContactService;
import com.Linko.services.ImageService;
import com.Linko.services.UserService;
import com.Linko.util.AppConstraint;
import com.Linko.util.Helper;
import com.Linko.util.MessageType;
import com.Linko.util.message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final ImageService imageService;
    private final ContactService contactService;

    // user dashbord page
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard() {
        return "user/dashboard";
    }

    @RequestMapping(value = "/direct", method = RequestMethod.GET)
    public String direct(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstraint.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model,
            Authentication authentication) {

        // load all the user contacts
        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);

        Page<Contact> pageContact = contactService.getByUser(user, page, size,
                sortBy, direction);

        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstraint.PAGE_SIZE);

        model.addAttribute("contactSearchForm", new ContactSearchForm());
        return "user/direct";
    }

    @RequestMapping(value = "/inbox", method = RequestMethod.GET)
    public String inbox() {
        return "user/inbox";
    }

    // profile page
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(Model model, Authentication authentication) {

        return "user/profile";
    }

    // user profile update view
    @GetMapping(value = "/profile/update/{loggedInUserId}")
    public String viewUpdateProfile(@PathVariable("loggedInUserId") String loggedInUserId,
            Model model) {

        User user = userService.getUserById(Helper.stringToLong(loggedInUserId)).orElseThrow(() -> {
            logger.error("User not found with id: {}", loggedInUserId);
            return new IllegalArgumentException("Invalid user Id:" + loggedInUserId);
        });
        ContactForm contactForm = new ContactForm();
        contactForm.setName(user.getName());

        contactForm.setEmail(user.getEmail());
        contactForm.setPhoneNumber(user.getPhoneNumber());
        contactForm.setAddress(user.getAddress());
        contactForm.setDescription(user.getAbout());
        contactForm.setPicture(user.getProfilePicture());

        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", loggedInUserId);

        return "user/update_profile_view";
    }

    // user profile update
    @RequestMapping(value = "profile/update/{loggedInUserId}", method = RequestMethod.POST)
    public String updateContact(@PathVariable("loggedInUserId") String loggedInUserId,
            @Valid @ModelAttribute ContactForm contactForm,
            BindingResult bindingResult,
            HttpSession session) {

        // update the contact
        if (bindingResult.hasErrors()) {
            return "user/update_profile_view";
        }

        User con = userService.getUserById(Helper.stringToLong(loggedInUserId)).orElseThrow(() -> {
            logger.error("User not found with id: {}", loggedInUserId);
            return new IllegalArgumentException("Invalid user Id:" + loggedInUserId);
        });
        // con.setId(contactId);
        con.setName(contactForm.getName());
        con.setEmail(contactForm.getEmail());
        con.setPhoneNumber(contactForm.getPhoneNumber());
        con.setAddress(contactForm.getAddress());
        con.setAbout(contactForm.getDescription());

        // process image:

        if (contactForm.getContactImage() != null &&
                !contactForm.getContactImage().isEmpty()) {
            logger.info("file is not empty");
            String fileName = UUID.randomUUID().toString();
            String imageUrl = imageService.uploadImage(contactForm.getContactImage(),
                    fileName);
            con.setCloudinaryImagePublicId(fileName);
            con.setProfilePicture(imageUrl);
            contactForm.setPicture(imageUrl);

        } else {
            logger.info("file is empty");
        }

        var updateCon = userService.updateUser(con);
        logger.info("updated contact {}", updateCon);

        session.setAttribute("message",
                message.builder().content("Contact Updated  !!").type(MessageType.green).build());

        return "/user/update_profile_view";
    }

}

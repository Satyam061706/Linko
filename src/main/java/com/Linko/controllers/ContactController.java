package com.Linko.controllers;

import java.util.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Linko.form.*;
import com.Linko.services.*;
import com.Linko.entities.*;
import com.Linko.util.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @RequestMapping("/add")
    // add contact page: handler
    public String addContactView(Model model) {
        ContactForm contactForm = new ContactForm();

        contactForm.setFavorite(true);
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    // add contact handler
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,
            Authentication authentication, HttpSession session) {

        // process the form data
        // 1 validate form
        if (result.hasErrors()) {

            result.getAllErrors().forEach(error -> logger.info(error.toString()));

            session.setAttribute("message", message.builder()
                    .content("Please correct the following errors")
                    .type(MessageType.red)
                    .build());
            return "user/add_contact";
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        // 2 process the contact
        // form ---> contact
        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());

        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {

            // 3 set the contact picture url
            String filename = UUID.randomUUID().toString();
            String fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);
            contact.setPicture(fileURL);
            contact.setCloudinaryImagePublicId(filename);

        }
        contactService.save(contact);
        System.out.println(contactForm);

        // 4 `set message to be displayed on the view

        session.setAttribute("message",
                message.builder()
                        .content("You have successfully added a new contact")
                        .type(MessageType.green)
                        .build());

        return "redirect:/user/contacts/add";

    }

    // view contacts handler
    @RequestMapping
    public String viewContacts(
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

        return "user/contacts";
    }

    // view contact details by id
    @RequestMapping("/view-contact/{contactId}")
    public String viewContact(
            @PathVariable("contactId") String contactId,
            Model model) {

        var contact = contactService.getById(contactId);
        model.addAttribute("contact", contact);

        return "user/view_contact";
    }

    // search handler
    @RequestMapping("/search")
    public String searchHandler(

            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "size", defaultValue = AppConstraint.PAGE_SIZE + "") int size,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model,
            Authentication authentication) {

        logger.info("field {} keyword {}", contactSearchForm.getField(),
                contactSearchForm.getValue());

        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Contact> pageContact = null;
        if (contactSearchForm.getField().equalsIgnoreCase("name")) {
            pageContact = contactService.searchByName(contactSearchForm.getValue(), size,
                    page, sortBy, direction,
                    user);
        } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
            pageContact = contactService.searchByEmail(contactSearchForm.getValue(),
                    size, page, sortBy, direction,
                    user);
        } else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
            pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page,
                    sortBy,
                    direction, user);
        }

        logger.info("pageContact {}", pageContact);

        model.addAttribute("contactSearchForm", contactSearchForm);

        model.addAttribute("pageContact", pageContact);

        model.addAttribute("pageSize", AppConstraint.PAGE_SIZE);

        return "user/search";
    }

    // search direct handler
    @RequestMapping("/direct/search")
    public String directSearchHandler(

            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "size", defaultValue = AppConstraint.PAGE_SIZE + "") int size,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model,
            Authentication authentication) {

        logger.info("field {} keyword {}", contactSearchForm.getField(),
                contactSearchForm.getValue());

        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Contact> pageContact = null;
        if (contactSearchForm.getField().equalsIgnoreCase("name")) {
            pageContact = contactService.searchByName(contactSearchForm.getValue(), size,
                    page, sortBy, direction,
                    user);
        } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
            pageContact = contactService.searchByEmail(contactSearchForm.getValue(),
                    size, page, sortBy, direction,
                    user);
        } else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
            pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page,
                    sortBy,
                    direction, user);
        }

        logger.info("pageContact {}", pageContact);

        model.addAttribute("contactSearchForm", contactSearchForm);

        model.addAttribute("pageContact", pageContact);

        model.addAttribute("pageSize", AppConstraint.PAGE_SIZE);

        return "user/direct_search";
    }

    // delete contact handler
    @DeleteMapping("/delete/{contactId}")
    @ResponseBody
    public ResponseEntity<Void> deleteContact(@PathVariable String contactId) {
        contactService.delete(contactId);
        return ResponseEntity.ok().build(); // ✅ 200 OK
    }

    // update contact form view
    @GetMapping("/view/{contactId}")
    public String updateContactFormView(
            @PathVariable("contactId") String contactId,
            Model model) {

        var contact = contactService.getById(contactId);
        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavorite(contact.isFavorite());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setPicture(contact.getPicture());

        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);

        return "user/update_contact_view";
    }

    // updating contact handler
    @RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
    public String updateContact(@PathVariable("contactId") String contactId,
            @Valid @ModelAttribute ContactForm contactForm,
            BindingResult bindingResult,
            HttpSession session) {

        // update the contact
        if (bindingResult.hasErrors()) {
            return "user/update_contact_view";
        }

        var con = contactService.getById(contactId);
        // con.setId(contactId);
        con.setName(contactForm.getName());
        con.setEmail(contactForm.getEmail());
        con.setPhoneNumber(contactForm.getPhoneNumber());
        con.setAddress(contactForm.getAddress());
        con.setDescription(contactForm.getDescription());
        con.setFavorite(contactForm.isFavorite());
        con.setWebsiteLink(contactForm.getWebsiteLink());
        con.setLinkedInLink(contactForm.getLinkedInLink());

        // process image:

        if (contactForm.getContactImage() != null &&
                !contactForm.getContactImage().isEmpty()) {
            logger.info("file is not empty");
            String fileName = UUID.randomUUID().toString();
            String imageUrl = imageService.uploadImage(contactForm.getContactImage(),
                    fileName);
            con.setCloudinaryImagePublicId(fileName);
            con.setPicture(imageUrl);
            contactForm.setPicture(imageUrl);

        } else {
            logger.info("file is empty");
        }

        var updateCon = contactService.update(con);
        logger.info("updated contact {}", updateCon);

        session.setAttribute("message",
                message.builder().content("Contact Updated  !!").type(MessageType.green).build());

        return "redirect:/user/contacts/view/" + contactId;
    }

    // send email form view
    // @GetMapping("/send-email/{userId}/{contactId}")
    // public String sendEmailFormView(
    // @PathVariable("contactId") String contactId,
    // @PathVariable("userId") String userId,
    // Model model) {

    // Contact contact = contactService.getById(contactId);
    // User user =
    // userService.getUserById(Helper.stringToLong(userId)).orElseThrow(() -> {
    // logger.error("User not found with id: {}", userId);
    // return new IllegalArgumentException("Invalid user Id:" + userId);
    // });

    // EmailForm emailForm = new EmailForm();
    // emailForm.setTo(contact.getEmail());
    // emailForm.setFrom(user.getEmail());

    // model.addAttribute("emailForm", emailForm);
    // model.addAttribute("contactId", contactId);
    // model.addAttribute("userId", userId);

    // return "user/send_email_view";
    // }

    // // sending email handler
    // @PostMapping("/send-email/{userId}/{contactId}")
    // public String sendEmail(@PathVariable("contactId") String contactId,
    // @PathVariable("userId") String userId, @Valid @ModelAttribute EmailForm
    // emailForm,
    // BindingResult bindingResult, HttpSession session,
    // Model model) {

    // // update the contact
    // if (bindingResult.hasErrors()) {
    // return "user/send_email_view";
    // }

    // Boolean flag = contactService.sendEmail(userId, contactId, emailForm);
    // if (flag) {
    // session.setAttribute("message",
    // message.builder()
    // .content("Email sent successfully !!")
    // .type(MessageType.green)
    // .build());
    // } else {
    // session.setAttribute("message",
    // message.builder()
    // .content("Failed to send email !!")
    // .type(MessageType.red)
    // .build());
    // }
    // // con.setId(contactId);
    // // con.setName(contactForm.getName());
    // // con.setEmail(contactForm.getEmail());
    // // con.setPhoneNumber(contactForm.getPhoneNumber());
    // // con.setAddress(contactForm.getAddress());
    // // con.setDescription(contactForm.getDescription());
    // // con.setFavorite(contactForm.isFavorite());
    // // con.setWebsiteLink(contactForm.getWebsiteLink());
    // // con.setLinkedInLink(contactForm.getLinkedInLink());

    // // process image:

    // // if (contactForm.getContactImage() != null &&
    // // !contactForm.getContactImage().isEmpty()) {
    // // logger.info("file is not empty");
    // // String fileName = UUID.randomUUID().toString();
    // // String imageUrl = imageService.uploadImage(contactForm.getContactImage(),
    // // fileName);
    // // con.setCloudinaryImagePublicId(fileName);
    // // con.setPicture(imageUrl);
    // // contactForm.setPicture(imageUrl);

    // // } else {
    // // logger.info("file is empty");
    // // }

    // // var updateCon = contactService.update(con);
    // // logger.info("updated contact {}", updateCon);

    // return "redirect:/user/contacts/send-email/" + userId + "/" + contactId;
    // }

    // // outbox emails view handler
    // @RequestMapping("/outbox-emails")
    // public String viewOutBoxEmails(
    // @RequestParam(value = "page", defaultValue = "0") int page,
    // @RequestParam(value = "size", defaultValue = AppConstraint.PAGE_SIZE + "")
    // int size,
    // @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
    // @RequestParam(value = "direction", defaultValue = "asc") String direction,
    // Model model,
    // Authentication authentication) {

    // // load all the user contacts
    // String username = Helper.getEmailOfLoggedInUser(authentication);

    // User user = userService.getUserByEmail(username);

    // Page<Contact> pageContact = contactService.getByUser(user, page, size,
    // sortBy, direction);

    // Page<Email> outboxEmails = contactService.getOutboxEmailsByUser(user, page,
    // size,
    // sortBy, direction);

    // model.addAttribute("pageContact", pageContact);
    // model.addAttribute("outboxEmails", outboxEmails);
    // model.addAttribute("pageSize", AppConstraint.PAGE_SIZE);

    // model.addAttribute("contactSearchForm", new ContactSearchForm());

    // return "user/outbox_email";
    // }

    // @DeleteMapping("/email/delete/{emailId}")
    // @ResponseBody
    // public ResponseEntity<Void> deleteEmail(@PathVariable String emailId) {
    // contactService.deleteEmail(emailId);
    // return ResponseEntity.ok().build();
    // }

    // // view email details by id
    // @RequestMapping("/view-email/{emailId}")
    // public String viewEmail(
    // @PathVariable("emailId") String emailId,
    // Model model) {

    // Email email = contactService.getEmailById(emailId);
    // model.addAttribute("email", email);
    // return "user/view_email";
    // }

}

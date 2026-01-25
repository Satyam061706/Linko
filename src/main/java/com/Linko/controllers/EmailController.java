package com.Linko.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Linko.entities.Contact;
import com.Linko.entities.Email;
import com.Linko.entities.User;
import com.Linko.form.ContactSearchForm;
import com.Linko.form.EmailForm;
import com.Linko.services.ContactService;
import com.Linko.services.ImageService;
import com.Linko.services.UserService;
import com.Linko.util.AppConstraint;
import com.Linko.util.Helper;
import com.Linko.util.MessageType;
import com.Linko.util.message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class EmailController {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    // send email form view
    @GetMapping("/send-email/{userId}/{contactId}")
    public String sendEmailFormView(
            @PathVariable("contactId") String contactId,
            @PathVariable("userId") String userId,
            Model model) {

        Contact contact = contactService.getById(contactId);
        User user = userService.getUserById(Helper.stringToLong(userId)).orElseThrow(() -> {
            logger.error("User not found with id: {}", userId);
            return new IllegalArgumentException("Invalid user Id:" + userId);
        });

        EmailForm emailForm = new EmailForm();
        emailForm.setTo(contact.getEmail());
        emailForm.setFrom(user.getEmail());

        model.addAttribute("emailForm", emailForm);
        model.addAttribute("contactId", contactId);
        model.addAttribute("userId", userId);

        return "user/send_email_view";
    }

    // sending email handler
    @PostMapping("/send-email/{userId}/{contactId}")
    public String sendEmail(@PathVariable("contactId") String contactId,
            @PathVariable("userId") String userId, @Valid @ModelAttribute EmailForm emailForm,
            BindingResult bindingResult, HttpSession session,
            Model model) {

        // update the contact
        if (bindingResult.hasErrors()) {
            return "user/send_email_view";
        }

        Boolean flag = contactService.sendEmail(userId, contactId, emailForm);
        if (flag) {
            session.setAttribute("message",
                    message.builder()
                            .content("Email sent successfully !!")
                            .type(MessageType.green)
                            .build());
        } else {
            session.setAttribute("message",
                    message.builder()
                            .content("Failed to send email !!")
                            .type(MessageType.red)
                            .build());
        }
        return "redirect:/user/contacts/send-email/" + userId + "/" + contactId;
    }

    // outbox emails view handler
    @RequestMapping("/outbox-emails")
    public String viewOutBoxEmails(
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

        Page<Email> outboxEmails = contactService.getOutboxEmailsByUser(user, page, size,
                sortBy, direction);

        model.addAttribute("pageContact", pageContact);
        model.addAttribute("outboxEmails", outboxEmails);
        model.addAttribute("pageSize", AppConstraint.PAGE_SIZE);

        model.addAttribute("contactSearchForm", new ContactSearchForm());

        return "user/outbox_email";
    }

    @DeleteMapping("/email/delete/{emailId}")
    @ResponseBody
    public ResponseEntity<Void> deleteEmail(@PathVariable String emailId) {
        contactService.deleteEmail(emailId);
        return ResponseEntity.ok().build();
    }

    // view email details by id
    @RequestMapping("/view-email/{emailId}")
    public String viewEmail(
            @PathVariable("emailId") String emailId,
            Model model) {

        Email email = contactService.getEmailById(emailId);
        model.addAttribute("email", email);
        return "user/view_email";
    }

}

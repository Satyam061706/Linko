package com.Linko.services.impl;

import com.Linko.entities.Contact;
import com.Linko.entities.Email;
import com.Linko.entities.User;
import com.Linko.exception.ResourceNotFoundException;
import com.Linko.form.EmailForm;
import com.Linko.repositories.ContactRepository;
import com.Linko.repositories.EmailRepository;
import com.Linko.repositories.UserRepositories;
import com.Linko.services.ContactService;
import com.Linko.util.Helper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService

{

    @Autowired
    private ContactRepository contactRepo;

    @Autowired
    private UserRepositories userRepo;

    @Autowired
    private EmailRepository emailRepo;

    @Autowired
    private EmailServiceImpl emailService;

    @Override
    public Contact save(Contact contact) {

        return contactRepo.save(contact);

    }

    @Override
    public Contact update(Contact contact) {

        Contact contactOld = contactRepo.findById(contact.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        contactOld.setName(contact.getName());
        contactOld.setEmail(contact.getEmail());
        contactOld.setPhoneNumber(contact.getPhoneNumber());
        contactOld.setAddress(contact.getAddress());
        contactOld.setDescription(contact.getDescription());
        contactOld.setPicture(contact.getPicture());
        contactOld.setFavorite(contact.isFavorite());
        contactOld.setWebsiteLink(contact.getWebsiteLink());
        contactOld.setLinkedInLink(contact.getLinkedInLink());
        contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());

        return contactRepo.save(contactOld);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(Helper.stringToLong(id))
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id " + id));
    }

    @Transactional
    @Override
    public void delete(String id) {

        Long contactId = Helper.stringToLong(id);

        Contact contact = contactRepo.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Contact not found with id " + id));

        User user = contact.getUser();
        user.getContacts().remove(contact);
        contact.setUser(null);
    }

    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepo.findByUserId(userId);

    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction) {

        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUser(user, pageable);

    }

    @Override
    public Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order, User user) {

        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndNameContaining(user, nameKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order,
            User user) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndEmailContaining(user, emailKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy,
            String order, User user) {

        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndPhoneNumberContaining(user, phoneNumberKeyword, pageable);
    }

    @Override
    public Boolean sendEmail(String userId, String contactId, EmailForm emailForm) {

        User user = userRepo.findById(Helper.stringToLong(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        Email email = new Email();
        email.setFrom(emailForm.getFrom());
        email.setTo(emailForm.getTo());
        email.setName(user.getName());
        email.setUser(user);
        email.setContent(emailForm.getContent());
        email.setSubject(emailForm.getSubject());

        emailRepo.save(email);

        emailService.sendEmailToContact(email);

        return true;
    }

    @Override
    public Page<Email> getOutboxEmailsByUser(User user, int page, int size, String sortBy, String direction) {

        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return emailRepo.findByUser(user, pageable);
    }

    @Override
    public void deleteEmail(String emailId) {

        Long id = Helper.stringToLong(emailId);

        try {
            Email email = emailRepo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Email not found with id " + emailId));

            User user = email.getUser();
            user.getOutboxEmails().remove(email);
            email.setUser(null);
            emailRepo.delete(email);
        } catch (Exception e) {
            throw new ResourceNotFoundException(
                    "Cannot delete the email because of exception" + emailId);
        }
    }

    @Override
    public Email getEmailById(String emailId) {

        return emailRepo.findById(Helper.stringToLong(emailId))
                .orElseThrow(() -> new ResourceNotFoundException("Email not found with given id " + emailId));
    }

}
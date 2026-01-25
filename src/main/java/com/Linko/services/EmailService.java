package com.Linko.services;

import com.Linko.entities.Email;

public interface EmailService {

    //
    void sendEmail(String to, String subject, String body);

    //
    void sendEmailWithHtml();

    //
    void sendEmailWithAttachment();

    void sendEmailToContact(Email email);

}
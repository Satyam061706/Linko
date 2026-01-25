package com.Linko;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.Linko.services.EmailService;

@SpringBootTest
class LinkoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private EmailService emailService;

	@Test
	public void testEmail() {
		// working
		// emailService.sendEmail("satyamsharma061706@gmail.com", "Linko email", "This
		// email is form linko application");

		// not working
		emailService.sendEmail("sharmassatyam@yahoo.com", "Linko application", "Thisemail is form linko application");

	}
}

package ca.gc.tri_agency.granting_data.controller;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SimpleEmailController {

	@Autowired
	private JavaMailSender sender;

	@RequestMapping("/viewFiscalYear")
	@ResponseBody
	String home() {
		try {
			sendEmail();
			return "Email Sent!";
		} catch (Exception ex) {
			return "Error in sending email: " + ex;
		}
	}

	private void sendEmail() throws Exception {
		try {
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setTo("yaserabidakadr@mailinator.com");
			helper.setText("How are you?");
			helper.setSubject("Hi");

			sender.send(message);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

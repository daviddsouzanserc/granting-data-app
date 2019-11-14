package ca.gc.tri_agency.granting_data.controller;

import org.springframework.stereotype.Controller;

@Controller
public class SimpleEmailController {

//	@Autowired
//	private JavaMailSender sender;
//
//	@RequestMapping("/viewFiscalYear")
//	@ResponseBody
//	String home() {
//		try {
//			sendEmail();
//			return "Email Sent!";
//		} catch (Exception ex) {
//			return "Error in sending email: " + ex;
//		}
//		// UserRepo.getAllPersons().toString();
//	}
//
//	private void sendEmail() throws Exception {
//		try {
//			MimeMessage message = sender.createMimeMessage();
//			MimeMessageHelper helper = new MimeMessageHelper(message);
//
//			helper.setTo("yaserabidakadr@mailinator.com");
//			helper.setText("How are you?");
//			helper.setSubject("Hi");
//
//			sender.send(message);
//
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//	}
}

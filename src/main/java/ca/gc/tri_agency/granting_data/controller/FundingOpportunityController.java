package ca.gc.tri_agency.granting_data.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.gc.tri_agency.granting_data.model.User;
import ca.gc.tri_agency.granting_data.repoLdap.UserRepo;

@Controller
@RequestMapping(value = "/fundingOpp", method = RequestMethod.GET)
public class FundingOpportunityController {
	@Autowired
	UserRepo userRepo;

	@GetMapping(value = "/searchUser")
	public String searchUserForm() {
		return "fundingOpp/searchUser";
	}

//	@GetMapping(value = "/searchUser", params = "username")
//	public String searchUserAction(@RequestParam("username") String username, Model model) {
//		List<String> matchingUsers = userRepo.search(username);
//		model.addAttribute("matchingUsers", matchingUsers);
//		return "fundingOpp/searchUser";
//	}

	@GetMapping(value = "/searchUser", params = "username")
	public String searchUserAction(@RequestParam("username") String username, Model model) {
		String matchingUsers = userRepo.getDnByUsername(username);
		model.addAttribute("matchingUsers", matchingUsers);
		return "fundingOpp/searchUser";
	}

	@GetMapping(value = "editProgramLead")
	public String editProgramLead(Model model) {
		List<User> matchingUsers = userRepo.getAllPersons();
		model.addAttribute("matchingUsers", matchingUsers);
		return "fundingOpp/changeProgramLead";
	}
}

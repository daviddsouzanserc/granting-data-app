package ca.gc.tri_agency.granting_data.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.gc.tri_agency.granting_data.service.UserRepo;

@Controller
@RequestMapping(value = "/fundingOpp", method = RequestMethod.GET)
public class FundingOpportunityController {
	@Autowired
	UserRepo userRepo;

	@GetMapping(value = "/searchUser")
	public String searchUserForm() {
		return "fundingOpp/searchUser";
	}

	@GetMapping(value = "/searchUser", params = "username")
	public String searchUserAction(@RequestParam("username") String username, Model model) {
		List<String> matchingUsers = userRepo.search(username);
		model.addAttribute("matchingUsers", matchingUsers);
		return "fundingOpp/searchUser";
	}

//	@GetMapping(value="/searchUser", params="username")
//	public String searchUserAction(@RequestParam("username") String username, Model model) {
//		List<String> matchingUsers = userService.getAllUsers();
//		model.addAttribute("matchingUsers",  matchingUsers);
//		return "fundingOpp/searchUser";
//	}
}

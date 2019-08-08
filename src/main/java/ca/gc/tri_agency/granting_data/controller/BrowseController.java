package ca.gc.tri_agency.granting_data.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.gc.tri_agency.granting_data.model.Agency;
import ca.gc.tri_agency.granting_data.model.FundingCycle;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.model.User;
import ca.gc.tri_agency.granting_data.repoLdap.UserRepo;
import ca.gc.tri_agency.granting_data.service.DataAccessService;
import ca.gc.tri_agency.granting_data.service.GoldenListService;
import ca.gc.tri_agency.granting_data.service.RestrictedDataService;

@Controller
@RequestMapping("/browse")
public class BrowseController {

//	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	GoldenListService goldenListService;

	@Autowired
	DataAccessService dataService;

	@Autowired
	RestrictedDataService restrictedDataService;

	@Autowired
	UserRepo userRepo;

	@GetMapping("/goldenList")
	public String goldListDisplay(Model model) {
		model.addAttribute("goldenList", goldenListService.getGoldenList());
		model.addAttribute("fcByFoMap", goldenListService.getFundingCycleByFundingOpportunityMap());

		return "browse/goldenList";
	}

	@GetMapping(value = "/viewFo")
	public String viewFundingOpportunity(@RequestParam("id") long id, Model model) {
		model.addAttribute("fo", dataService.getFundingOpportunity(id));
		model.addAttribute("fundingCycles", dataService.getSystemFundingCyclesByFoId(id));
		return "browse/viewFundingOpportunity";
	}

	@GetMapping(value = "/editFo")
	public String editFo(@RequestParam("id") long id, Model model) {
		FundingOpportunity fo = dataService.getFundingOpportunity(id);
		model.addAttribute("fo", fo);
		model.addAttribute("programForm", fo);

		List<Agency> allAgencies = dataService.getAllAgencies();
		List<Agency> otherAgencies = new ArrayList<Agency>();
		for (Agency a : allAgencies) {
			if (fo.getParticipatingAgencies().contains(a) == false) {
				otherAgencies.add(a);
			}
		}
		model.addAttribute("otherAgencies", otherAgencies);
		model.addAttribute("allAgencies", allAgencies);
		return "browse/editFundingOpportunity";
	}

	@PostMapping(value = "/editFo")
	public String editFoPost(@Valid @ModelAttribute("programForm") FundingOpportunity command,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			// model.addAttribute("allAgencies", dataService.getAllAgencies());
			for (ObjectError br : bindingResult.getAllErrors()) {
				System.out.println(br.toString());
			}
			return "redirect:/browse/goldenList";
		}
		restrictedDataService.saveFundingOpportunity(command);
		return "redirect:/browse/viewFo?id=" + command.getId();
	}

	@GetMapping(value = "editProgramLead")
	public String editProgramLead(@RequestParam("id") long id, Model model) {
		model.addAttribute("originalId", id);
		return "browse/editProgramLead";
	}

	@GetMapping(value = "editProgramLead", params = "username")
	public String editProgramLeadSearchUser(@RequestParam("id") long id, @RequestParam("username") String username,
			Model model) {
		List<User> matchingUsers = userRepo.searchOther(username);
		model.addAttribute("matchingUsers", matchingUsers);
		model.addAttribute("originalId", id);
		return "browse/editProgramLead";
	}

//	@PostMapping(value = "/editProgramLead")
//	public String editProgramLeadPost(@RequestParam long foId, @RequestParam String leadUserDn) {
//		// get the FO based on the ID
//		// get the AD person based on the leadUserDn
//		// in the FO, lead name and lead DN, save the FO
//		// service.setFoLeadContributor(long foId, leadUserDn)
//		restrictedDataService.setFoLeadContributor(foId, leadUserDn);
//		return "redirect:/browse/viewFo?id=" + foId;
//	}

	@PostMapping(value = "/editProgramLead")
	public String editProgramLeadPost(@RequestParam long foId, @RequestParam User user) {
		restrictedDataService.setFoLeadContributor(foId, user);
		return "redirect:/browse/viewFo?id=" + foId;
	}

	@GetMapping(value = "createFundingCycle")
	public String createFundingCycle(@RequestParam("id") long id, Model model) {
		model.addAttribute("foId", id);
		model.addAttribute("fundingCycle", new FundingCycle());
		return "browse/createFundingCycle";
	}

	@PostMapping(value = "createFundingCycle")
	public String createFundingCyclePost(@Valid @ModelAttribute("fundingCycle") FundingCycle command,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			for (ObjectError br : bindingResult.getAllErrors()) {
				System.out.println(br.toString());
			}
			return "browse/createFundingCycle";
		}
		restrictedDataService.createOrUpdateFundingCycle(command);

		return "redirect:/browse/viewFo?id=" + command.getFundingOpportunity().getId();
	}

}

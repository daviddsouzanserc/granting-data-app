package ca.gc.tri_agency.granting_data.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.gc.tri_agency.granting_data.model.Agency;
import ca.gc.tri_agency.granting_data.model.FiscalYear;
import ca.gc.tri_agency.granting_data.model.FundingCycle;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.model.GrantingCapability;
import ca.gc.tri_agency.granting_data.model.User;
import ca.gc.tri_agency.granting_data.repo.GrantingStageRepository;
import ca.gc.tri_agency.granting_data.repo.GrantingSystemRepository;
import ca.gc.tri_agency.granting_data.repoLdap.UserRepo;
import ca.gc.tri_agency.granting_data.service.DataAccessService;
import ca.gc.tri_agency.granting_data.service.RestrictedDataService;

@Controller
@RequestMapping(value = "/manage", method = RequestMethod.GET)
public class ManageFundingOpportunityController {
	@Autowired
	UserRepo userRepo;

	@Autowired
	RestrictedDataService restrictedDataService;

	@Autowired
	DataAccessService dataService;
	@Autowired
	GrantingSystemRepository grantingSystemRepo;

	@Autowired
	GrantingStageRepository grantingStageRepo;

	@GetMapping(value = "/searchUser")
	public String searchUserForm() {
		return "fundingOpp/searchUser";
	}

	@GetMapping(value = "/manageFo")
	public String viewFundingOpportunity(@RequestParam("id") long id, Model model) {
		model.addAttribute("fo", dataService.getFundingOpportunity(id));
		model.addAttribute("fundingCycles", dataService.getFundingCyclesByFoId(id));
		model.addAttribute("systemFundingCycles", dataService.getSystemFundingCyclesByFoId(id));
		model.addAttribute("grantingCapabilities", dataService.getGrantingCapabilitiesByFoId(id));
		return "manage/manageFundingOpportunity";
	}

	@GetMapping(value = "/searchUser", params = "username")
	public String searchUserAction(@RequestParam("username") String username, Model model) {
		String matchingUsers = userRepo.getDnByUsername(username);
		model.addAttribute("matchingUsers", matchingUsers);
		return "manage/searchUser";
	}

	@GetMapping(value = "/editFo", params = "id")
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
		return "manage/editFundingOpportunity";
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

	//////////////////////

	@GetMapping(value = "/editFc", params = "id")
	public String editFc(@RequestParam("id") long id, Model model) {
		model.addAttribute("fc", dataService.getFundingCycle(id));
		return "manage/editFundingCycle";
	}

	@PostMapping(value = "/editFc")
	public String createFundingCyclePost2(@Valid @ModelAttribute("fc") FundingCycle command,
			BindingResult bindingResult) {
		FundingCycle target = dataService.getFundingCycle(command.getId());
		if (bindingResult.hasErrors()) {
			for (ObjectError br : bindingResult.getAllErrors()) {
				System.out.println(br.toString());
			}
			return "manage/editFundingCycle";
		}
		restrictedDataService.updateFc(command, target);

		return "redirect:/browse/viewFo?id=" + target.getFundingOpportunity().getId();
	}

/////////////////////////

	@GetMapping(value = "/editProgramLead", params = "id")
	public String editProgramLead(@RequestParam("id") long id, Model model) {
		model.addAttribute("originalId", id);
		List<User> matchingUsers = userRepo.getAllPersons();
		model.addAttribute("matchingUsers", matchingUsers);
		return "manage/editProgramLead";
	}

	@GetMapping(value = "/editProgramLead", params = { "id", "username" })
	public String editProgramLeadSearchUser(@RequestParam("id") long id, @RequestParam("username") String username,
			Model model) {
		List<User> matchingUsers = userRepo.searchOther(username);
		model.addAttribute("matchingUsers", matchingUsers);
		model.addAttribute("originalId", id);
		return "manage/editProgramLead";
	}

	@PostMapping(value = "/editProgramLead")
	public String editProgramLeadPost(@RequestParam long foId, @RequestParam String leadUserDn) {
		// get the FO based on the ID
		// get the AD person based on the leadUserDn
		// in the FO, lead name and lead DN, save the FO
		// service.setFoLeadContributor(long foId, leadUserDn)
		restrictedDataService.setFoLeadContributor(foId, leadUserDn);
		return "redirect:/browse/viewFo?id=" + foId;
	}

	@GetMapping(value = "/createFundingCycle", params = "id")
	public String createFundingCycle(@RequestParam("id") long id, Model model) {
		model.addAttribute("foId", id);
		model.addAttribute("fundingCycle", new FundingCycle());
		model.addAttribute("fy", dataService.findAllFiscalYears());
		return "manage/createFundingCycle";
	}

	@PostMapping(value = "/createFundingCycle")
	public String createFundingCyclePost(@Valid @ModelAttribute("fundingCycle") FundingCycle command,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			for (ObjectError br : bindingResult.getAllErrors()) {
				System.out.println(br.toString());
			}
			return "manage/createFundingCycle";
		}
		restrictedDataService.createOrUpdateFundingCycle(command);

		return "redirect:/browse/viewFo?id=" + command.getFundingOpportunity().getId();
	}

	@GetMapping(value = "/addGrantingCapabilities", params = "id")
	public String addGrantingCapabilities(@RequestParam("id") long id, Model model) {
		model.addAttribute("foId", id);
		model.addAttribute("gc", new GrantingCapability());
		model.addAttribute("grantingSystems", grantingSystemRepo.findAll());
		model.addAttribute("grantingStages", grantingStageRepo.findAll());
		return "manage/addGrantingCapabilities";
	}

	@PostMapping(value = "/addGrantingCapabilities")
	public String addGrantingCapabilitiesPost(@Valid @ModelAttribute("gc") GrantingCapability command,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			for (ObjectError br : bindingResult.getAllErrors()) {
				System.out.println(br.toString());
			}
			return "manage/addGrantingCapabilities";
		}
		restrictedDataService.createGrantingCapability(command);

		return "redirect:/browse/viewFo?id=" + command.getFundingOpportunity().getId();
	}

	@GetMapping(value = "/addFiscalYears", params = "id")
	public String addFiscalYears(Model model) {
		model.addAttribute("fiscalYears", dataService.findAllFiscalYears());
		model.addAttribute("fy", new FiscalYear());
		return "manage/addFiscalYears";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/addFiscalYears")
	public String addFiscalYearsPost(@Valid @ModelAttribute("fy") FiscalYear command, BindingResult bindingResult,
			Model model) throws Exception {
		if (bindingResult.hasErrors()) {
			System.out.println(bindingResult.getFieldError().toString());

		}

		try {
			dataService.createFy(command.getYear());
		}

		catch (Exception e) {
			model.addAttribute("error", "Your input is not valid!"
					+ " Please make sure to input a year between 1999 and 2050 that was not created before");
			return "manage/addFiscalYears";

		}

		return "redirect:/browse/viewFiscalYear";
	}

	@GetMapping(value = "/addFo")
	public String addFo(Model model) {
		List<Agency> allAgencies = dataService.getAllAgencies();
		model.addAttribute("fo", new FundingOpportunity());
		model.addAttribute("allAgencies", allAgencies);
		return "manage/addFo";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/addFo", params = "id")
	public String addFoPost(@Valid @ModelAttribute("fo") FundingOpportunity command, BindingResult bindingResult,
			Model model) throws Exception {
		if (bindingResult.hasErrors()) {
			System.out.println(bindingResult.getFieldError().toString());

		}

		try {
			dataService.createFo(command);
		}

		catch (Exception e) {
			model.addAttribute("error", "Your input is not valid!"
					+ " Please make sure to input a year between 1999 and 2050 that was not created before");
			return "manage/addFo";

		}

		return "redirect:/browse/goldenList";
	}

}

package ca.gc.tri_agency.granting_data.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.gc.tri_agency.granting_data.form.ProgramForm;
import ca.gc.tri_agency.granting_data.model.Agency;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
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

	@GetMapping("/goldenList")
	public String goldListDisplay(Model model) {
		model.addAttribute("goldenList", goldenListService.getGoldenList());
		model.addAttribute("fcByFoMap", goldenListService.getFundingCycleByFundingOpportunityMap());

		return "browse/goldenList";
	}

	@GetMapping(value = "/viewFo")
	public String viewFundingOpportunity(@RequestParam("id") long id, Model model) {
		model.addAttribute("fo", dataService.getFundingOpportunity(id));
		return "browse/viewFundingOpportunity";
	}

	@GetMapping(value = "/editFo")
	public String editFo(@RequestParam("id") long id, Model model) {
		FundingOpportunity fo = dataService.getFundingOpportunity(id);
		model.addAttribute("fo", fo);
		model.addAttribute("programForm", new ProgramForm(fo));

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

//	@PostMapping(value = "/editFo")
//	public String editProgramPost(@Valid @ModelAttribute("fo") FundingOpportunity command // Model
//																							// model,
//			, BindingResult bindingResult) {
//
//		if (bindingResult.hasErrors()) {
//			// model.addAttribute("allAgencies", dataService.getAllAgencies());
//			return "browse/viewFundingOpportunity";
//		}
//		FundingOpportunity targetUpdate = dataService.getFundingOpportunity(command.getId());
//		targetUpdate.loadFromForm(command);
//		restrictedDataService.saveFundingOpportunity(targetUpdate);
//		return "redirect:viewProgram?id=" + targetUpdate.getId();
//	}

	@PostMapping(value = "/editFo")
	public String editProgramPost(@Valid @ModelAttribute("programForm") ProgramForm command,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			// model.addAttribute("allAgencies", dataService.getAllAgencies());
			return "browse/viewFundingOpportunity";
		}
		FundingOpportunity targetUpdate = dataService.getFundingOpportunity(command.getId());
		targetUpdate.loadFromForm(targetUpdate);
		restrictedDataService.saveFundingOpportunity(targetUpdate);
		return "redirect:/viewFo?id=\" + targetUpdate.getId()";
	}

}

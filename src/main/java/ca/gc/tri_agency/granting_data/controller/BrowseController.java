package ca.gc.tri_agency.granting_data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.gc.tri_agency.granting_data.service.DataAccessService;

@Controller
@RequestMapping("/browse")
public class BrowseController {

	// private static final Logger LOG = LogManager.getLogger();

	@Autowired
	DataAccessService dataService;

	@GetMapping("/goldenList")
	public String goldListDisplay(Model model) {
		model.addAttribute("goldenList", dataService.getAllFundingOpportunities());
		model.addAttribute("fcByFoMap", dataService.getFundingCycleByFundingOpportunityMap());

		return "browse/goldenList";
	}

	@GetMapping(value = "/viewFo")
	public String viewFundingOpportunity(@RequestParam("id") long id, Model model) {
		model.addAttribute("fo", dataService.getFundingOpportunity(id));
		// model.addAttribute("systemFoCycles",
		// dataService.getSystemFundingCyclesByFoId(id));
		model.addAttribute("grantingCapabilities", dataService.getGrantingCapabilitiesByFoId(id));
		model.addAttribute("fcDataMap", dataService.getFundingCycleDataMapByYear(id));
		return "browse/viewFundingOpportunity";
	}

}

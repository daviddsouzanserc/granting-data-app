package ca.gc.tri_agency.granting_data.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.service.GoldenListService;

@Controller
@RequestMapping("/browse")
public class BrowseController {

//	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	GoldenListService goldenListService;

	@GetMapping("/goldenList")
	public String goldListDisplay(Model model) {
		model.addAttribute("goldenList", goldenListService.getGoldenList());
		model.addAttribute("fcByFoMap", goldenListService.getFundingCycleByFundingOpportunityMap());

		return "browse/goldenList";
	}

	@GetMapping(value = "/selectFo")
	public String selectFoDisplay() {
		return "browse/selectFo";
	}

	@GetMapping(value = "/selectFo", params = "name")
	public String selectedFoDisplay(@RequestParam("name") String name, Model model) {
		model.addAttribute("selectedFoName", name);
		List<FundingOpportunity> fos = goldenListService.getFoByNameEn(name);
		model.addAttribute("fos", fos);
//		model.addAttribute("fo", fos.get(0));
//		LOG.log(Level.INFO, "" + fos.size());
		return "browse/selectFo";
	}

}

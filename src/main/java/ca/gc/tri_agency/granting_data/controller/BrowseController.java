package ca.gc.tri_agency.granting_data.controller;

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

import ca.gc.tri_agency.granting_data.model.FiscalYear;
import ca.gc.tri_agency.granting_data.model.util.CalendarGrid;
import ca.gc.tri_agency.granting_data.service.DataAccessService;

@Controller
@RequestMapping("/browse")
public class BrowseController {

	// private static final Logger LOG = LogManager.getLogger();

	@Autowired
	DataAccessService dataService;

	@GetMapping(value = "/viewAgency")
	public String viewAgency(@RequestParam("id") long id, Model model) {
		model.addAttribute("agency", dataService.getAgency(id));
		model.addAttribute("agencyFos", dataService.getAgencyFundingOpportunities(id));
		return "browse/viewAgency";
	}

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

	@GetMapping(value = "/viewCalendar")
	public String viewCalendar(@RequestParam(name = "plusMinusMonth", defaultValue = "0") Long plusMinusMonth,
			Model model) {
		model.addAttribute("plusMonth", plusMinusMonth + 1);
		model.addAttribute("minusMonth", plusMinusMonth - 1);
		model.addAttribute("calGrid", new CalendarGrid(plusMinusMonth));
		model.addAttribute("fcCalEvents", dataService.getMonthlyFundingCyclesMapByDate(plusMinusMonth));
		return "browse/viewCalendar";
	}

	@GetMapping(value = "/viewFiscalYear")
	public String viewFundingCycles(Model model) {
		model.addAttribute("fiscalYear", dataService.findAllFiscalYears());
		model.addAttribute("fy", new FiscalYear());
		return "browse/viewFiscalYear";
	}

	@GetMapping(value = "/viewFcFromFy")
	public String viewFundingCyclesFromFiscalYear(@RequestParam("id") long id, Model model) {
		model.addAttribute("fc", dataService.fundingCyclesByFiscalYearId(id));
		return "browse/viewFcFromFy";
	}

	@PostMapping(value = "/viewFiscalYear")
	public String addFiscalYearPost(@Valid @ModelAttribute("fy") FiscalYear command, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			for (ObjectError br : bindingResult.getAllErrors()) {
				System.out.println(br.toString());
			}
			return "redirect:/browse/viewFiscalYear";
		}

		List<FiscalYear> fy = dataService.findAllFiscalYears();
		Long year = command.getYear();
		boolean exist = false;

		for (FiscalYear f : fy) {
			if (f.getYear().equals(year)) {
				exist = true;
				break;
			}
		}

		if (!exist) {
			dataService.createFy(year);
		}
		return "redirect:/browse/viewFiscalYear";
	}

}

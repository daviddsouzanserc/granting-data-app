package ca.gc.tri_agency.granting_data.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ca.gc.tri_agency.granting_data.model.BusinessUnit;
import ca.gc.tri_agency.granting_data.security.annotations.AdminOnly;
import ca.gc.tri_agency.granting_data.service.BusinessUnitService;
import ca.gc.tri_agency.granting_data.service.DataAccessService;

@Controller
public class BusinessUnitController {

	private BusinessUnitService buService;
	private DataAccessService das; // TODO: refactor DataAccessService

	@Autowired
	private MessageSource msgSource;

	@Autowired
	public BusinessUnitController(BusinessUnitService buService, DataAccessService das) {
		this.buService = buService;
		this.das = das;
	}

	@GetMapping("/browse/viewBU")
	public String showViewBU(@RequestParam("id") Long id, Model model) {
		model.addAttribute("bu", buService.findBusinessUnitById(id));
		return "browse/viewBU";
	}

	@AdminOnly
	@GetMapping("/admin/createBU")
	public String showCreateBU(@RequestParam("agencyId") Long agencyId, Model model) {
		BusinessUnit bu = new BusinessUnit();
		bu.setAgency(das.getAgency(agencyId));
		model.addAttribute("bu", bu);
		return "admin/createBU";
	}

	@AdminOnly
	@PostMapping(value = "/admin/createBU")
	public String processCreateBU(@Valid @ModelAttribute("bu") BusinessUnit bu, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "admin/createBU";
		}
		buService.saveBusinessUnit(bu);
		String actionMsg = msgSource.getMessage("h.createdBu", null, LocaleContextHolder.getLocale());
		redirectAttributes.addFlashAttribute("actionMsg", actionMsg + bu.getName());
		return "redirect:/browse/viewAgency?id=" + bu.getAgency().getId();
	}

	@AdminOnly
	@GetMapping("/admin/editBU")
	public String showEditBU(@RequestParam("id") Long id, Model model) {
		model.addAttribute("bu", buService.findBusinessUnitById(id));
		return "admin/editBU";
	}

	@AdminOnly
	@PostMapping("/admin/editBU")
	public String processEditBU(@Valid @ModelAttribute("bu") BusinessUnit bu, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "admin/editBU";
		}
		buService.saveBusinessUnit(bu);
		String actionMsg = msgSource.getMessage("h.editedBu", null, LocaleContextHolder.getLocale());
		redirectAttributes.addFlashAttribute("actionMsg", actionMsg + bu.getName());
		return "redirect:/browse/viewAgency?id=" + bu.getAgency().getId();
	}

}

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

import ca.gc.tri_agency.granting_data.model.GrantingCapability;
import ca.gc.tri_agency.granting_data.repo.GrantingStageRepository;
import ca.gc.tri_agency.granting_data.repo.GrantingSystemRepository;
import ca.gc.tri_agency.granting_data.security.annotations.AdminOnly;
import ca.gc.tri_agency.granting_data.service.GrantingCapabilityService;

@Controller
public class GrantingCapabilityController {

	private GrantingCapabilityService gcService;
	private GrantingStageRepository gStageRepo; // TODO: REFACTOR
	private GrantingSystemRepository gSystemRepo; // TODO: REFACTOR

	@Autowired
	private MessageSource msgSource;

	@Autowired
	public GrantingCapabilityController(GrantingCapabilityService gcService, GrantingStageRepository gStageRepo,
			GrantingSystemRepository gSystemRepo) {
		this.gcService = gcService;
		this.gStageRepo = gStageRepo;
		this.gSystemRepo = gSystemRepo;
	}

	@AdminOnly
	@GetMapping("/manage/editGC")
	public String viewEditGC(@RequestParam("id") Long id, Model model) {
		model.addAttribute("gc", gcService.findGrantingCapabilityById(id));
		model.addAttribute("grantingStages", gStageRepo.findAll());
		model.addAttribute("grantingSystems", gSystemRepo.findAll());
		return "manage/editGrantingCapability";
	}

	@AdminOnly
	@PostMapping("/manage/editGC")
	public String processEditGC(@Valid @ModelAttribute("gc") GrantingCapability gc, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("grantingStages", gStageRepo.findAll());
			model.addAttribute("grantingSystems", gSystemRepo.findAll());
			return "manage/editGrantingCapability";
		}
		gcService.saveGrantingCapability(gc);
		String actionMsg = msgSource.getMessage("h.editedGc", null, LocaleContextHolder.getLocale());
		redirectAttributes.addFlashAttribute("actionMsg", actionMsg);
		return "redirect:/manage/manageFo?id=" + gc.getFundingOpportunity().getId();
	}

	@AdminOnly
	@GetMapping("/manage/deleteGC")
	public String viewDeleteGC(@RequestParam("id") Long id, Model model) {
		model.addAttribute("gc", gcService.findGrantingCapabilityById(id));
		return "manage/deleteGrantingCapability";
	}

	@AdminOnly
	@PostMapping("/manage/deleteGC")
	public String processDeleteGC(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
		String foId = gcService.findGrantingCapabilityById(id).getFundingOpportunity().getId().toString();
		gcService.deleteGrantingCapabilityById(id);
		String actionMsg = msgSource.getMessage("h.deletedGC", null, LocaleContextHolder.getLocale());
		redirectAttributes.addFlashAttribute("actionMsg", actionMsg);
		return "redirect:/manage/manageFo?id=" + foId;
	}

}

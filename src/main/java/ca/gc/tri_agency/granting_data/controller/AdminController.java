package ca.gc.tri_agency.granting_data.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import ca.gc.tri_agency.granting_data.model.Agency;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.model.SystemFundingOpportunity;
import ca.gc.tri_agency.granting_data.model.file.FundingCycleDatasetRow;
import ca.gc.tri_agency.granting_data.security.annotations.AdminOnly;
import ca.gc.tri_agency.granting_data.service.AdminService;
import ca.gc.tri_agency.granting_data.service.DataAccessService;

@Controller
@RequestMapping("/admin")
@AdminOnly
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private DataAccessService dataSevice;

	@Autowired
	private MessageSource msgSource;

	@GetMapping(value = "/confirmUnlink")
	public String unlinkSfoFromFo_get(@RequestParam long sfoId, Model model) {
		SystemFundingOpportunity sfo = dataSevice.getSystemFO(sfoId);
		FundingOpportunity fo = sfo.getLinkedFundingOpportunity();
		if (fo == null) {
			throw new DataRetrievalFailureException(
					"That System Funding Opportunity is not linked to a Funding Opportunity");
		}
		model.addAttribute("sfo", sfo);
		model.addAttribute("fo", fo);

		return "/admin/confirmUnlink";
	}

	@PostMapping(value = "/confirmUnlink")
	public String unlinkSfoFromFo_post(@RequestParam long sfoId, Model model, RedirectAttributes redirectAttributes) {
		SystemFundingOpportunity sfo = dataSevice.getSystemFO(sfoId);
		FundingOpportunity fo = dataSevice.getSystemFO(sfoId).getLinkedFundingOpportunity();

		adminService.unlinkSystemFO(sfoId, fo.getId());

		String wasUnlinkedFrom = msgSource.getMessage("msg.unlinkedPerformedMsg", null,
				LocaleContextHolder.getLocale());
		redirectAttributes.addFlashAttribute("actionMessage",
				sfo.getLocalizedAttribute("name") + wasUnlinkedFrom + fo.getLocalizedAttribute("name"));

		model.addAttribute("systemFO", sfo);
		model.addAttribute("fosForLink", dataSevice.getAllFundingOpportunities());

		return "redirect:/admin/viewSystemFO?id=" + sfoId;
	}

	@GetMapping("/selectFileForComparison")
	public String compareData_selectDatasetUploadFile(Model model) {
		model.addAttribute("datasetFiles", adminService.getDatasetFiles());
		return "admin/selectFileForComparison";
	}

	@GetMapping(value = "/analyzeFoUploadData", params = "filename")
	public String compareData_showDatasetUploadAdditions(@RequestParam String filename, Model model) {
		model.addAttribute("filename", filename);
		List<FundingCycleDatasetRow> fileRows = adminService.getFundingCyclesFromFile(filename);
		model.addAttribute("fileRows", fileRows);
		model.addAttribute("actionRowIds", adminService.generateActionableFoCycleIds(fileRows));
		return "admin/analyzeFoUploadData";
	}

	@PostMapping("/analyzeFoUploadData")
	public String compareData_uploadSelectedNames_post(@RequestParam String filename,
			@RequestParam("idToAction") String[] idsToAction, final RedirectAttributes redirectAttrs) {
		long numChances = adminService.applyChangesFromFileByIds(filename, idsToAction);
		redirectAttrs.addFlashAttribute("actionMessage", "Successfully applied " + numChances + " Funcing Cycles");
		return "redirect:/admin/home";
	}

	@GetMapping("/home")
	public String home(Model model) {

		return "admin/home";

	}

	@GetMapping("/analyzeSystemFOs")
	public String analyzeSystemFOs(Model model) {
		model.addAttribute("systemFOs", dataSevice.getAllSystemFOs());
		return "admin/analyzeSystemFOs";
	}

	@GetMapping("/viewSystemFO")
	public String viewSystemFO(@RequestParam Long id, Model model, HttpServletRequest request) {
		Map<String, ?> inFlashMap = RequestContextUtils.getInputFlashMap(request);
		if (inFlashMap != null) {
			model.addAttribute("unlinkedPerformedMsg", inFlashMap.get("actionMessage"));
		}

		model.addAttribute("systemFO", dataSevice.getSystemFO(id));
		model.addAttribute("fosForLink", dataSevice.getAllFundingOpportunities());
		return "admin/viewSystemFO";
	}

	@PostMapping(value = "/registerFOLink")
	public String registerProgramLinkPost(@ModelAttribute("id") Long id, @ModelAttribute("foId") Long foId) {
		adminService.linkSystemFO(id, foId);
		return "redirect:analyzeSystemFOs";
	}

	@GetMapping(value = "/createFo")
	public String addFo(Model model, @RequestParam(name = "sfoId", required = false) Optional<Long> sfoId) {
		FundingOpportunity fo = new FundingOpportunity();
		if (sfoId.isPresent()) {
			SystemFundingOpportunity sfo = dataSevice.getSystemFO(sfoId.get());
			fo.setNameEn(sfo.getNameEn());
			fo.setNameFr(sfo.getNameFr());
		}
		List<Agency> allAgencies = dataSevice.getAllAgencies();
		model.addAttribute("fo", fo);
		model.addAttribute("allAgencies", allAgencies);
		return "admin/createFo";
	}

	@PostMapping(value = "/createFo", params = "id")
	public String addFoPost(@Valid @ModelAttribute("fo") FundingOpportunity command, BindingResult bindingResult,
			Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (bindingResult.hasErrors()) {
			// required in order to re-populate the drop-down list
			List<Agency> allAgencies = dataSevice.getAllAgencies();
			model.addAttribute("allAgencies", allAgencies);
			return "admin/createFo";
		}
		dataSevice.createFo(command);
		String createdFo = msgSource.getMessage("h.createdFo", null, LocaleContextHolder.getLocale());
		redirectAttributes.addFlashAttribute("actionMessage", createdFo + command.getLocalizedAttribute("name"));
		return "redirect:/admin/home";
	}

}

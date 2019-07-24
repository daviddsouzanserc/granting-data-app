package ca.gc.tri_agency.granting_data.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ca.gc.tri_agency.granting_data.model.file.FundingCycleDatasetRow;
import ca.gc.tri_agency.granting_data.service.AdminService;
import ca.gc.tri_agency.granting_data.service.DataAccessService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminService adminService;

	@Autowired
	DataAccessService dataSevice;

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

	@GetMapping("/importProgramsFromFile")
	public String importPrograms() {
		return "admin/importPrograms";
	}

	@PostMapping("/importProgramsFromFile")
	public String importPrograms_post(Model model, final RedirectAttributes redirectAttrs) {
		int num = adminService.importProgramsFromFile();
		redirectAttrs.addFlashAttribute("actionMessage", "Successfully applied " + num + " Funcing Cycles");
		return "redirect:/admin/home";
	}

	@GetMapping("/analyzeSystemFOs")
	public String analyzeSystemFOs(Model model) {
		model.addAttribute("systemFOs", dataSevice.getAllSystemFOs());
		return "admin/analyzeSystemFOs";
	}

	@GetMapping("/viewSystemFO")
	public String viewSystemFO(@RequestParam Long id, Model model) {
		model.addAttribute("systemFO", dataSevice.getSystemFO(id));
		model.addAttribute("fosForLink", dataSevice.getAllFundingOpportunities());
		return "admin/viewSystemFo";
	}

	@PostMapping(value = "/registerFOLink")
	public String registerProgramLinkPost(@ModelAttribute("id") Long id, @ModelAttribute("foId") Long foId) {
		adminService.linkSystemFO(id, foId);
		return "redirect:analyzeSystemFOs";
	}

}

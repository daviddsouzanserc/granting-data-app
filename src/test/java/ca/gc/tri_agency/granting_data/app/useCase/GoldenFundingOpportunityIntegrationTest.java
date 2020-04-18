package ca.gc.tri_agency.granting_data.app.useCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ca.gc.tri_agency.granting_data.app.GrantingDataApp;
import ca.gc.tri_agency.granting_data.controller.AdminController;
import ca.gc.tri_agency.granting_data.model.Agency;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.model.SystemFundingOpportunity;
import ca.gc.tri_agency.granting_data.repo.AgencyRepository;
import ca.gc.tri_agency.granting_data.repo.FundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.service.DataAccessService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrantingDataApp.class)
@ActiveProfiles("local")
public class GoldenFundingOpportunityIntegrationTest {

	@Autowired
	private AdminController adminController;
	@Autowired
	private AgencyRepository agencyRepo;
	@Autowired
	private FundingOpportunityRepository foRepo;
	@Autowired
	SystemFundingOpportunityRepository sfoRepo;
	@Autowired
	private DataAccessService dataAccessService;
	@Autowired
	private WebApplicationContext ctx;

	@Mock
	private BindingResult bindingResult;
	@Mock
	private Model model;
	@Mock
	private RedirectAttributes redirectAttributes;

	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(ctx).apply(SecurityMockMvcConfigurers.springSecurity()).build();
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	@Transactional(readOnly = true)
	public void testNameFieldsEmptyOnAddFoPageWhenNewSfoNotLinkedWithSfo() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/admin/createFo"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		assertTrue(result.getResponse().getContentAsString()
				.contains("<input class=\"col-sm-2\" id=\"nameEn\" name=\"nameEn\" value=\"\""));
		assertTrue(result.getResponse().getContentAsString()
				.contains("<input class=\"col-sm-2\" id=\"nameFr\" name=\"nameFr\" value=\"\""));
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	@Transactional
	public void testNameFieldsAutoFilledOnAddFoPageWhenLinkingNewFoWithSfo() throws Exception {
		SystemFundingOpportunity sfo = sfoRepo.findAll().get(0);
		Long sfoId = sfo.getId();

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/admin/createFo").param("sfoId", sfoId.toString()))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		assertTrue(result.getResponse().getContentAsString()
				.contains("<input class=\"col-sm-2\" id=\"nameEn\" name=\"nameEn\" value=\"" + sfo.getNameEn() + '"'));
		assertTrue(result.getResponse().getContentAsString()
				.contains("<input class=\"col-sm-2\" id=\"nameFr\" name=\"nameFr\" value=\"" + sfo.getNameFr() + '"'));
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	@Transactional(readOnly = true)
	public void test_NonAdminCannotCreateGoldenFo_shouldFailWith403() throws Exception {
		boolean cpx = true;
		String div = RandomStringUtils.randomAlphabetic(10);
		boolean edi = true;
		String frequency = RandomStringUtils.randomAlphabetic(10);
		String ft = RandomStringUtils.randomAlphabetic(10);
		boolean loi = true;
		boolean noi = true;
		boolean ji = true;
		String nameEn = RandomStringUtils.randomAlphabetic(25);
		String nameFr = RandomStringUtils.randomAlphabetic(25);
		String po = RandomStringUtils.randomAlphabetic(25);
		String pld = RandomStringUtils.randomAlphabetic(25);
		String pln = RandomStringUtils.randomAlphabetic(25);
		List<Agency> agencyList = agencyRepo.findAll();
		Agency la = agencyList.size() > 0 ? agencyList.remove(0) : null;
//		Set<Agency> pas = agencyList.size() > 0 ? new HashSet<>(agencyList) : null;

		List<FundingOpportunity> fos = foRepo.findAll();
		String idParam = String.valueOf(fos.get(fos.size() - 1).getId() + 1L);

		mvc.perform(MockMvcRequestBuilders.post("/admin/createFo").param("id", idParam).param("nameEn", nameEn)
				.param("nameFr", nameFr).param("leadAgency", Long.toString(la.getId())).param("division", div)
				.param("isJointInitiative", Boolean.toString(ji)).param("fundingType", ft).param("partnerOrg", po)
				.param("frequency", frequency).param("isComplex", Boolean.toString(cpx))
				.param("isEdiRequired", Boolean.toString(edi)).param("isNOI", Boolean.toString(noi))
				.param("isLOI", Boolean.toString(loi)).param("programLeadName", pln).param("programLeadDn", pld))
				.andExpect(MockMvcResultMatchers.status().isForbidden()).andExpect(MockMvcResultMatchers.content()
						.string(Matchers.containsString("id=\"forbiddenByRoleErrorPage\"")));
	}

	@WithMockUser(roles = "MDM ADMIN")
	@Test
	@Transactional
	public void test_AdminCanCreateGoldenFo_usingMvcPerform_shouldSucceed() throws Exception {
		String am = RandomStringUtils.randomAlphabetic(10);
		String ams = RandomStringUtils.randomAlphabetic(10);
		boolean cpx = true;
		String div = RandomStringUtils.randomAlphabetic(10);
		boolean edi = true;
		String frequency = RandomStringUtils.randomAlphabetic(10);
		String ft = RandomStringUtils.randomAlphabetic(10);
		boolean loi = true;
		boolean noi = true;
		boolean ji = true;
		String nameEn = RandomStringUtils.randomAlphabetic(25);
		String nameFr = RandomStringUtils.randomAlphabetic(25);
		String po = RandomStringUtils.randomAlphabetic(25);
		String pld = RandomStringUtils.randomAlphabetic(25);
		String pln = RandomStringUtils.randomAlphabetic(25);
		List<Agency> agencyList = agencyRepo.findAll();
		Agency la = agencyList.size() > 0 ? agencyList.remove(0) : null;
//		Set<Agency> pas = agencyList.size() > 0 ? new HashSet<>(agencyList) : null;

		List<FundingOpportunity> fos = foRepo.findAll();
		String idParam = String.valueOf(fos.get(fos.size() - 1).getId() + 1L);

		mvc.perform(MockMvcRequestBuilders.post("/admin/createFo").param("id", idParam).param("nameEn", nameEn)
				.param("nameFr", nameFr).param("leadAgency", Long.toString(la.getId())).param("division", div)
				.param("isJointInitiative", Boolean.toString(ji)).param("fundingType", ft).param("partnerOrg", po)
				.param("frequency", frequency).param("isComplex", Boolean.toString(cpx))
				.param("isEdiRequired", Boolean.toString(edi)).param("isNOI", Boolean.toString(noi))
				.param("isLOI", Boolean.toString(loi)).param("programLeadName", pln).param("programLeadDn", pld))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/admin/home")).andExpect(MockMvcResultMatchers.flash()
						.attribute("actionMessage", "Created Funding Opportunity named: " + nameEn));

		// when the page is refreshed, the flash attribute should disappear
		mvc.perform(MockMvcRequestBuilders.get("/admin/home"))
				.andExpect(MockMvcResultMatchers.flash().attributeCount(0));

		fos = foRepo.findAll();
		FundingOpportunity newGfo = fos.get(fos.size() - 1);

		assertEquals(idParam, String.valueOf(newGfo.getId()));
		assertEquals(cpx, newGfo.getIsComplex());
		assertEquals(div, newGfo.getDivision());
		assertEquals(edi, newGfo.getIsEdiRequired());
		assertEquals(frequency, newGfo.getFrequency());
		assertEquals(ft, newGfo.getFundingType());
		assertEquals(loi, newGfo.getIsLOI());
		assertEquals(noi, newGfo.getIsNOI());
		assertEquals(ji, newGfo.getIsJointInitiative());
		assertEquals(la, newGfo.getLeadAgency());
		assertEquals(nameEn, newGfo.getNameEn());
		assertEquals(nameFr, newGfo.getNameFr());
//		assertEquals(pas, newGfo.getParticipatingAgencies());
		assertEquals(po, newGfo.getPartnerOrg());
		assertEquals(pld, newGfo.getProgramLeadDn());
		assertEquals(pln, newGfo.getProgramLeadName());
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test(expected = AccessDeniedException.class)
	@Transactional
	public void test_NonAdminCannotCreateGoldenFo_shouldThrowDataAccessException() throws Exception {
		adminController.addFoPost(new FundingOpportunity(), bindingResult, model, redirectAttributes);
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test(expected = AccessDeniedException.class)
	@Transactional
	public void test_NonAdminCannotCreateGoldenFo_shouldThrowAccessDeniedException() {
		dataAccessService.createFo(new FundingOpportunity());
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	@Transactional
	public void test_AdminCanCreateGoldenFo_shouldSucceed() throws Exception {
		long initFoRepoSize = foRepo.count();

		FundingOpportunity gfo = new FundingOpportunity();
		gfo.setIsComplex(false);
		gfo.setDivision(RandomStringUtils.randomAlphabetic(10));
		gfo.setIsEdiRequired(false);
		gfo.setFrequency(RandomStringUtils.randomAlphabetic(10));
		gfo.setFundingType(RandomStringUtils.randomAlphabetic(10));
		gfo.setIsLOI(false);
		gfo.setIsNOI(false);
		gfo.setIsJointInitiative(true);
		gfo.setNameEn(RandomStringUtils.randomAlphabetic(25));
		gfo.setNameFr(RandomStringUtils.randomAlphabetic(25));
		gfo.setPartnerOrg(RandomStringUtils.randomAlphabetic(25));
		gfo.setProgramLeadDn(RandomStringUtils.randomAlphabetic(25));
		gfo.setProgramLeadName(RandomStringUtils.randomAlphabetic(25));
		List<Agency> agencyList = agencyRepo.findAll();
		gfo.setLeadAgency(agencyList.size() > 0 ? agencyList.remove(0) : null);
		gfo.setParticipatingAgencies(agencyList.size() > 0 ? new HashSet<Agency>(agencyList) : null);

		String successUrl = adminController.addFoPost(gfo, bindingResult, model, redirectAttributes);

		assertEquals("redirect:/admin/home", successUrl);
		assertEquals(initFoRepoSize + 1, foRepo.count());
	}

}
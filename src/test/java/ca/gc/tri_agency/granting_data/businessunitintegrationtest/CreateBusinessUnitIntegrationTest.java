package ca.gc.tri_agency.granting_data.businessunitintegrationtest;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ca.gc.tri_agency.granting_data.app.GrantingDataApp;
import ca.gc.tri_agency.granting_data.model.Agency;
import ca.gc.tri_agency.granting_data.model.BusinessUnit;
import ca.gc.tri_agency.granting_data.repo.AgencyRepository;
import ca.gc.tri_agency.granting_data.repo.BusinessUnitRepository;
import ca.gc.tri_agency.granting_data.service.BusinessUnitService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrantingDataApp.class)
@ActiveProfiles("local")
public class CreateBusinessUnitIntegrationTest {

	@Autowired
	private BusinessUnitRepository buRepo;
	@Autowired
	private AgencyRepository agencyRepo;

	@Autowired
	private BusinessUnitService buService;

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	// CREATE LINK ACCESSIBLE VROM VIEW AGENCY PAGE, ONLY ACCESSIBLE BY ADMIN
	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void test_createBULinkVisibleToAdminOnViewAgencyPage_shouldSucceedWith200() throws Exception {
		mvc.perform(get("/browse/viewAgency?id=1")).andExpect(status().isOk()).andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString("id=\"createBusinessUnit\"")));
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	public void test_createBULinkNotVisibleToNonAdminOnViewAgencyPage_shouldSucceedReturn200() throws Exception {
		mvc.perform(get("/browse/viewAgency?id=1")).andExpect(status().isOk()).andExpect(
				MockMvcResultMatchers.content().string(not(Matchers.containsString("id=\"createBusinessUnit\""))));
	}

	// CREATE PAGE CAN ONLY BE ACCESSED BY ADMIN
	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void test_adminCanAccessCreateBUPage_shouldSucceedWith200() throws Exception {
		String agencyName = agencyRepo.findById(1L).get().getNameEn();
		assertTrue(mvc.perform(get("/admin/createBU?agencyId=1")).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString().contains("<div>" + agencyName + "</div>"));
	}

	// CREATE PAGE CANNOT BE ACCESSED BY NON-ADMIN
	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	public void test_nonAdmminCannotAccessCreateBUPage_Return403() throws Exception {
		assertTrue(mvc.perform(get("/admin/createBU?agencyId=1")).andExpect(status().isForbidden()).andReturn().getResponse()
				.getContentAsString().contains("id=\"forbiddenByRoleErrorPage\""));
	}

	// CREATE SERVICE TEST THAT VERIFIES THAT ONLY ADMIN USER CAN CREATE A BUSINESS UNIT
	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void testSerivce_adminCanCreateBU() {
		long initBuCount = buRepo.count();

		Agency agency = agencyRepo.findAll().get(0);
		BusinessUnit bu = new BusinessUnit("EN NAME TEST", "FR NAME TEST", "EN ACRONYM TEST", "FR ACRONYM TEST", agency);
		buService.saveBusinessUnit(bu);

		assertEquals(initBuCount + 1, buRepo.count());
	}

	// CREATE SERVICE TEST THAT VERIFIES THAT A NON-ADMIN USER CANNOT CREATE A BUSINESS UNIT
	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test(expected = AccessDeniedException.class)
	public void testService_nonAdminCannotCreateBU_shouldthrowAccessDeniedException() {
		Agency agency = agencyRepo.findAll().get(0);
		BusinessUnit bu = new BusinessUnit("EN NAME TEST", "FR NAME TEST", "EN ACRONYM TEST", "FR ACRONYM TEST", agency);
		buService.saveBusinessUnit(bu);
	}

	// CREATE POST ACTION CAN ONLY BE EXECUTED BY ADMIN
	@WithMockUser(roles = { "MDM ADMIN" })
	@Test
	public void testController_adminCanCreateBU_shouldSucceedWith302() throws Exception {
		long initBuCount = buRepo.count();

		String nameEn = RandomStringUtils.randomAlphabetic(20);
		String nameFr = RandomStringUtils.randomAlphabetic(20);
		String acronymEn = RandomStringUtils.randomAlphabetic(5);
		String acronymFr = RandomStringUtils.randomAlphabetic(5);
		Long agencyId = 1L;

		mvc.perform(MockMvcRequestBuilders.post("/admin/createBU").param("agencyId", Long.toString(agencyId))
				.param("nameEn", nameEn).param("nameFr", nameFr).param("acronymEn", acronymEn)
				.param("acronymFr", acronymFr).param("agency", Long.toString(agencyId)))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/browse/viewAgency?id=" + Long.toString(agencyId)))
				.andExpect(MockMvcResultMatchers.flash().attribute("actionMsg",
						"Created the Business Unit named: " + nameEn));

		// when viewBU page is refreshed, flash attribute should disappear
		mvc.perform(MockMvcRequestBuilders.get("/browse/viewAgency?id=" + Long.toString(agencyId)))
				.andExpect(MockMvcResultMatchers.flash().attributeCount(0));

		assertEquals(initBuCount + 1, buRepo.count());

		BusinessUnit addedBu = buRepo.findAll().get((int) initBuCount);
		assertEquals(nameEn, addedBu.getNameEn());
		assertEquals(nameFr, addedBu.getNameFr());
		assertEquals(acronymEn, addedBu.getAcronymEn());
		assertEquals(acronymFr, addedBu.getAcronymFr());
		assertEquals(agencyId, addedBu.getAgency().getId());
	}

	// CREATE POST ACTION CANNOT BE EXECUTED BY NON-ADMIN
	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	public void testController_nonAdminCannotCreateBU_shouldReturn403() throws Exception {
		long initBuCount = buRepo.count();

		String nameEn = RandomStringUtils.randomAlphabetic(20);
		String nameFr = RandomStringUtils.randomAlphabetic(20);
		String acronymEn = RandomStringUtils.randomAlphabetic(5);
		String acronymFr = RandomStringUtils.randomAlphabetic(5);
		Long agencyId = 1L;

		assertTrue(mvc.perform(MockMvcRequestBuilders.post("/admin/createBU").param("agencyId", Long.toString(agencyId))
				.param("nameEn", nameEn).param("nameFr", nameFr).param("acronymEn", acronymEn)
				.param("acronymFr", acronymFr).param("agency", Long.toString(agencyId)))
				.andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn().getResponse().getContentAsString()
				.contains("id=\"forbiddenByRoleErrorPage\""));

		assertEquals(initBuCount, buRepo.count());
	}

}

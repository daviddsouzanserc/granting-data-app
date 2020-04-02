package ca.gc.tri_agency.granting_data.app.useCase.manageBusinessUnits;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
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
import ca.gc.tri_agency.granting_data.service.impl.AdminServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrantingDataApp.class)
@ActiveProfiles("test")
public class PBI_19048_CreateBusinessUnitTest {

	@Autowired
	private BusinessUnitRepository buRepo;
	@Autowired
	private AgencyRepository agencyRepo;

	@Autowired
	private AdminServiceImpl asi;

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
	public void test_accessCreateBusinessUnitPageByAdmin_shouldSucceedWith200() throws Exception {
		String agencyName = agencyRepo.findById(1L).get().getNameEn();
		mvc.perform(get("/admin/createBU?agencyId=1")).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString()
				.contains("<input class=\"col-sm-2\" id=\"agencyNameLabel\" name=\"agencyNameLabel\" value=\""
						+ agencyName + '"');
		// TODO: refactor so that the Agency name label is filled in
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	public void test_accessCreateBusinessUnitByNonAdmin_shouldReturn403() throws Exception {
		assertTrue(mvc.perform(get("/admin/createBU?agencyId=1")).andExpect(status().isForbidden())
				.andReturn().getResponse().getContentAsString().contains("id=\"forbiddenByRoleErrorPage\""));
	}

	// CREATE POST ACTION CAN ONLY BE EXECUTED BY ADMIN
	@WithMockUser(roles = { "MDM ADMIN" })
	@Test
	public void test_onlyAdminUserCanCreateBusinessUnits_shouldSucceedWith302() throws Exception {
		long initBuCount = buRepo.count();

		String nameEn = RandomStringUtils.randomAlphabetic(20);
		String nameFr = RandomStringUtils.randomAlphabetic(20);
		String acronymEn = RandomStringUtils.randomAlphabetic(5);
		String acronymFr = RandomStringUtils.randomAlphabetic(5);
		Long agencyId = 1L;

		mvc.perform(MockMvcRequestBuilders.post("admin/createBU").param("nameEn", nameEn).param("nameFr", nameFr)
				.param("acronymEn", acronymEn).param("acronymFr", acronymFr))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.forwardedUrl("/browse/viewAgency?id=" + Long.toString(agencyId)))
				.andExpect(MockMvcResultMatchers.flash().attribute("actionMessage",
						"Created Business Unit named: " + nameEn));

		// when viewBU page is refreshed, flash attribute should disappear
		mvc.perform(MockMvcRequestBuilders.get("/browse/viewAgency?id=1"))
				.andExpect(MockMvcResultMatchers.flash().attributeCount(0));

		assertEquals(initBuCount + 1, buRepo.count());

		BusinessUnit addedBu = buRepo.findAll().get((int) initBuCount);
		assertEquals(nameEn, addedBu.getNameEn());
		assertEquals(nameFr, addedBu.getNameFr());
		assertEquals(acronymEn, addedBu.getAcronymEn());
		assertEquals(acronymFr, addedBu.getAcronymFr());
		assertEquals(agencyId, addedBu.getAgency().getId());
	}

	// CREATE SERVICE LAYER TEST THAT VERIFIES THAT AN ADMIN USER CAN CREATE OR UPDATE A BUSINESS UNIT
	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void test_onlyAdminUserCanCreateOrUpdateBusinessUnits() {
		long initBuCount = buRepo.count();

		Agency agency = agencyRepo.findAll().get(0);
		BusinessUnit bu = new BusinessUnit("EN NAME TEST", "FR NAME TEST", "EN ACRONYM TEST", "FR ACRONYM TEST", agency);
		asi.createOrUpdateBusinessUnit(bu);

		assertEquals(initBuCount + 1, buRepo.count());
	}

	// CREATE SERVICE LAYER TEST THAT VERIFIES THAT A NON-ADMIN USER CANNOT CREATE OR UPDATE A BUSINESS
	// UNIT
	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test(expected = AccessDeniedException.class)
	public void test_nonAdminUserCannotCreateOrUpdateBusinessUnits_shouldThrowAccessDeniedException() {
		Agency agency = agencyRepo.findAll().get(0);
		BusinessUnit bu = new BusinessUnit("EN NAME TEST", "FR NAME TEST", "EN ACRONYM TEST", "FR ACRONYM TEST", agency);
		asi.createOrUpdateBusinessUnit(bu);
	}

	// CREATE CONTROLLER TEST THAT VERIFIES THAT AN ADMIN USER CAN UPDATE A BUSINESS UNIT
	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void test_onlyAdminUserCanUpdateBusinessUnits_shouldSucceedWith302() throws Exception {
		long initBuRepoCount = buRepo.count();
		BusinessUnit buBeforeUpate = buRepo.findById(1L).get();

		String nameEn = RandomStringUtils.randomAlphabetic(20);
		String nameFr = RandomStringUtils.randomAlphabetic(20);
		String acronymEn = RandomStringUtils.randomAlphabetic(5);
		String acronymFr = RandomStringUtils.randomAlphabetic(5);
		Long agencyId = 2L;

		mvc.perform(MockMvcRequestBuilders.post("/admin/editBU").param("id", "1").param("nameEn", nameEn)
				.param("nameFr", nameFr).param("acronymEn", acronymEn).param("acronymFr", acronymFr))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.forwardedUrl("/browse/viewAgency?id=" + Long.toString(agencyId)))
				.andExpect(MockMvcResultMatchers.flash().attribute("actionMessage",
						"Edit Business Unit named: " + nameEn));

		// when viewBUs page is refreshed, flash attribute should disappear
		mvc.perform(MockMvcRequestBuilders.get("/browse/viewAgency?id=" + Long.toString(agencyId)))
				.andExpect(MockMvcResultMatchers.flash().attributeCount(0));

		assertEquals(initBuRepoCount, buRepo.count());

		// BusinessUnit after update
		BusinessUnit buAfterUpdate = buRepo.findById(1L).get();
		assertNotEquals(buBeforeUpate, buAfterUpdate);
		assertEquals(buBeforeUpate.getId(), buAfterUpdate.getId());
	}

	// CREATE CONTROLLER TEST THAT VERIFIES THAT A NON-ADMIN USER CANNOT UPDATE A BUSINESS UNIT
	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	public void test_nonAdminUserCannotAccessEditBusinessUnitPage_shouldReturn403() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/admin/editBU").param("id", "1"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	// CREATE TEST THAT VERIFIES THAT ANYONE CAN VIEW A BUSINESS UNIT
	@WithAnonymousUser
	@Test
	public void test_anonUserCanAccessViewBusinessUnitPage_shouldSucceedWith200() throws Exception {
		assertTrue("Wrong Page or Page is Missing Content", mvc.perform(MockMvcRequestBuilders.get("/browse/viewBU?id=1"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString()
				.contains("id=\"viewBUPage\""));
		
	}
}

package ca.gc.tri_agency.granting_data.businessunitintegrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ca.gc.tri_agency.granting_data.app.GrantingDataApp;
import ca.gc.tri_agency.granting_data.model.BusinessUnit;
import ca.gc.tri_agency.granting_data.repo.BusinessUnitRepository;
import ca.gc.tri_agency.granting_data.service.BusinessUnitService;

@SpringBootTest(classes = GrantingDataApp.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
public class EditBusinessUnitIntegrationTest {

	@Autowired
	private BusinessUnitService buService;
	@Autowired
	private BusinessUnitRepository buRepo;

	@Autowired
	private WebApplicationContext ctx;

	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(ctx).apply(SecurityMockMvcConfigurers.springSecurity()).build();
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void test_editBULinkVisibileToAdmin_shouldSucceedWith200() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/browse/viewBU").param("id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("id=\"editBULink\"")));
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	public void test_editBULinkNotVisibileToNonAdmin_shouldReturn200() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/browse/viewBU").param("id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content()
						.string(Matchers.not(Matchers.containsString("id=\"editBULink\""))));
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void test_adminCanAccessEditBUPage_shouldSucceedWith200() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/admin/editBU").param("id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("id=\"editBUPage\"")));
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	public void test_nonAdminCannotAccessEditBUPage_shouldReturn403() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/admin/editBU").param("id", "1"))
				.andExpect(MockMvcResultMatchers.status().isForbidden()).andExpect(MockMvcResultMatchers.content()
						.string(Matchers.containsString("id=\"forbiddenByRoleErrorPage\"")));
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void testService_adminCanEditBU() {
		long initBuRepoCount = buRepo.count();
		BusinessUnit buBeforeUpate = buRepo.findById(1L).get();
		BusinessUnit buAfterUpdate = buRepo.findById(1L).get();

		assertEquals(buBeforeUpate, buAfterUpdate);

		buAfterUpdate.setNameEn(RandomStringUtils.randomAlphabetic(20));
		buAfterUpdate.setNameFr(RandomStringUtils.randomAlphabetic(20));
		buAfterUpdate.setAcronymEn(RandomStringUtils.randomAlphabetic(5));
		buAfterUpdate.setAcronymFr(RandomStringUtils.randomAlphabetic(5));

		buService.saveBusinessUnit(buAfterUpdate);

		assertEquals(initBuRepoCount, buRepo.count());
		assertNotEquals(buBeforeUpate, buAfterUpdate);
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test(expected = AccessDeniedException.class)
	public void testService_nonAdminCannotEditBU() {
		BusinessUnit bu = buRepo.findById(1L).get();
		bu.setNameEn(RandomStringUtils.randomAlphabetic(20));
		bu.setNameFr(RandomStringUtils.randomAlphabetic(20));
		bu.setAcronymEn(RandomStringUtils.randomAlphabetic(5));
		bu.setAcronymFr(RandomStringUtils.randomAlphabetic(5));

		buService.saveBusinessUnit(bu);
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void testController_adminCanEditBU_shouldSucceedWith302() throws Exception {
		long initBuRepoCount = buRepo.count();
		BusinessUnit buBeforeUpate = buRepo.findById(1L).get();

		String nameEn = RandomStringUtils.randomAlphabetic(20);
		String nameFr = RandomStringUtils.randomAlphabetic(20);
		String acronymEn = RandomStringUtils.randomAlphabetic(5);
		String acronymFr = RandomStringUtils.randomAlphabetic(5);
		String agencyId = "2";

		mvc.perform(MockMvcRequestBuilders.post("/admin/editBU").param("id", Long.toString(buBeforeUpate.getId()))
				.param("nameEn", nameEn).param("nameFr", nameFr).param("acronymEn", acronymEn)
				.param("acronymFr", acronymFr).param("agencyId", agencyId))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/browse/viewAgency?id=" + agencyId))
				.andExpect(MockMvcResultMatchers.flash().attribute("actionMsg",
						"Edit the Business Unit named: " + nameEn));

		// when viewBUs page is refreshed, flash attribute should disappear
		mvc.perform(MockMvcRequestBuilders.get("/browse/viewAgency?id=" + agencyId))
				.andExpect(MockMvcResultMatchers.flash().attributeCount(0));

		assertEquals(initBuRepoCount, buRepo.count());

		// BusinessUnit after update
		BusinessUnit buAfterUpdate = buRepo.findById(1L).get();
		assertNotEquals(buBeforeUpate, buAfterUpdate);
		assertEquals(buBeforeUpate.getId(), buAfterUpdate.getId());
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	public void testController_nonAdminCannotEditBU_shouldReturn403() throws Exception {
		long initBuRepoCount = buRepo.count();
		BusinessUnit buBeforeFailedUpate = buRepo.findById(1L).get();

		String nameEn = RandomStringUtils.randomAlphabetic(20);
		String nameFr = RandomStringUtils.randomAlphabetic(20);
		String acronymEn = RandomStringUtils.randomAlphabetic(5);
		String acronymFr = RandomStringUtils.randomAlphabetic(5);
		String agencyId = "2";

		mvc.perform(MockMvcRequestBuilders.post("/admin/editBU").param("id", "1").param("nameEn", nameEn)
				.param("nameFr", nameFr).param("acronymEn", acronymEn).param("acronymFr", acronymFr)
				.param("agencyId", agencyId)).andExpect(MockMvcResultMatchers.status().isForbidden());

		assertEquals(initBuRepoCount, buRepo.count());

		BusinessUnit buAfterFailedUpdate = buRepo.findById(1L).get();
		assertEquals(buBeforeFailedUpate, buAfterFailedUpdate);
		assertEquals(buBeforeFailedUpate.getId(), buAfterFailedUpdate.getId());
	}

}

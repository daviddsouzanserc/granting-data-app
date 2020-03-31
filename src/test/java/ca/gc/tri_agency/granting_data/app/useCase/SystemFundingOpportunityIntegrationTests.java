package ca.gc.tri_agency.granting_data.app.useCase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import ca.gc.tri_agency.granting_data.app.GrantingDataApp;
import ca.gc.tri_agency.granting_data.repo.FundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingOpportunityRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GrantingDataApp.class)
@ActiveProfiles("test")
public class SystemFundingOpportunityIntegrationTests {

	@Autowired
	private WebApplicationContext ctx;

	@Autowired
	private SystemFundingOpportunityRepository sfoRepo;

	@Autowired
	private FundingOpportunityRepository foRepo;

	private MockMvc mvc;

	/*
	 * All test data comes from src/main/resources/db/h2-only-data.xml
	 */
	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(ctx).apply(SecurityMockMvcConfigurers.springSecurity()).build();
	}

	@Test
	@Transactional
	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	public void test_adminCanUnlinkFoFromSfo_shouldRedirectToViewSystemFo() throws Exception {
		assertTrue(sfoRepo.getOne(1L).getLinkedFundingOpportunity() != null);
		mvc.perform(MockMvcRequestBuilders.post("/admin/confirmUnlink").param("sfoId", "1"))
				.andExpect(MockMvcResultMatchers.flash().attributeCount(1))
				.andExpect(MockMvcResultMatchers.flash().attributeExists("actionMessage"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/admin/viewSystemFO?id=1"));
		assertTrue(sfoRepo.getOne(1L).getLinkedFundingOpportunity() == null);
		// when the page is refreshed, the flash attribute should disappear
		mvc.perform(MockMvcRequestBuilders.get("/admin/confirmUnlink").param("sfoId", "1"))
				.andExpect(MockMvcResultMatchers.flash().attributeCount(0));
	}

	@Test
	@Transactional(readOnly = true)
	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	public void test_unlinkFoConfirmationPageAccessableByAdmin_shouldSucceedWith200() throws Exception {
		String sfoName = sfoRepo.getOne(1L).getNameEn();
		String foName = sfoRepo.getOne(1L).getLinkedFundingOpportunity().getNameEn();
		assertTrue(mvc.perform(MockMvcRequestBuilders.get("/admin/confirmUnlink").param("sfoId", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn().getResponse().getContentAsString()
				.contains("Are you sure you want to unlink the System Funding Opportunity named " + sfoName
						+ " from the Funding Opportunity named " + foName + '?'));
	}

	@Test
	@Transactional(readOnly = true)
	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	public void test_unlinkFoConfirmationPageNotAccessableByNonAdminUsers_shouldReturn403() throws Exception {
		assertNotNull(sfoRepo.getOne(1L).getLinkedFundingOpportunity());
		assertTrue(mvc.perform(MockMvcRequestBuilders.get("/admin/confirmUnlink").param("sfoId", "1"))
				/*
				 * If status().isForbidden() is changed to status().isOk() then this test will
				 * pass but is that what you want?
				 */
				.andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn().getResponse().getContentAsString()
				.contains("id=\"forbiddenByRoleErrorPage\""));
	}

	@Test
	@Transactional(readOnly = true)
	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	public void test_confirmUnlinkPageNotAccessibleWhenSfoHasNoLinkedFo_shouldReturn404() throws Exception {
		assertNull(sfoRepo.getOne(2L).getLinkedFundingOpportunity());
		assertTrue(mvc.perform(MockMvcRequestBuilders.get("/admin/confirmUnlink").param("sfoId", "2"))
				.andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn().getResponse().getContentAsString()
				.contains("id=\"generalErrorPage\""));
	}

	@Test
	@Transactional(readOnly = true)
	@WithMockUser(roles = { "MDM ADMIN" })
	public void test_unlinkFoBtnNotVisibleToAdminWhenSfoHasNoLinkedFo() throws Exception {
		assertNull(sfoRepo.getOne(2L).getLinkedFundingOpportunity());
		assertFalse(mvc.perform(MockMvcRequestBuilders.get("/admin/viewSystemFO").param("id", "2"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString()
				.contains("id=\"unlinkSfoBtn\""));
	}

	@Test
	@Transactional(readOnly = true)
	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	public void test_unlinkFoBtnNotVisibleToNonAdminUsers() throws Exception {
		assertNotNull(sfoRepo.getOne(1L).getLinkedFundingOpportunity());
		assertFalse(mvc.perform(MockMvcRequestBuilders.get("/admin/viewSystemFO").param("id", "1"))
				.andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn().getResponse().getContentAsString()
				.contains("id=\"unlinkSfoBtn\""));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	public void test_unlinkFoBtnVisibleToAdminWhenFoLinkedToSfo() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/admin/viewSystemFO").param("id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("id=\"unlinkSfoBtn\"")));
	}

}

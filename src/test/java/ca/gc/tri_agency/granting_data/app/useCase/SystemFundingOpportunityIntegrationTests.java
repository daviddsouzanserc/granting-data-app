package ca.gc.tri_agency.granting_data.app.useCase;

import static org.junit.Assert.assertFalse;
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
	 * All test data is added through the src/main/resources/db/h2-only-data.xml
	 * file
	 */
	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(ctx).apply(SecurityMockMvcConfigurers.springSecurity()).build();
	}

	@Test
	@Transactional
	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	public void test_adminCanUnlinkFoFromSfo_shouldSucceedWith200() throws Exception {
		assertTrue(sfoRepo.getOne(1L).getLinkedFundingOpportunity() != null);
		mvc.perform(MockMvcRequestBuilders.post("/admin/confirmUnlink").param("sfoId", "1"))
				.andExpect(MockMvcResultMatchers.flash().attribute("linkedFoId", null))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/admin/viewSystemFO"))
				.andExpect(MockMvcResultMatchers.status().isOk());
		assertTrue(sfoRepo.getOne(1L).getLinkedFundingOpportunity() == null);
	}

	@Test
	@Transactional(readOnly = true)
	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	public void test_unlinkFoConfirmationPageAccessableByAdmin_shouldSucceedWith200() throws Exception {
		String sfoName = sfoRepo.getOne(1L).getNameEn();
		String foName = foRepo.getOne(26L).getNameEn();
		mvc.perform(MockMvcRequestBuilders.get("/admin/confirmUnlinkOfFO").param("sfoId", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString()
				.contains("Are you sure you want to unlink the " + foName + " from " + sfoName + "?");
	}

	@Test
	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	public void test_unlinkFoBtnNotVisibleToAdminWhenFoNotLinkedToSfo() throws Exception {
		assertFalse(mvc.perform(MockMvcRequestBuilders.get("/admin/viewSystemFO").param("id", "2"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString()
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

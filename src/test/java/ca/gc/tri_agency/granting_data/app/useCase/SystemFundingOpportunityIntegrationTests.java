package ca.gc.tri_agency.granting_data.app.useCase;

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
import org.springframework.web.context.WebApplicationContext;

import ca.gc.tri_agency.granting_data.app.GrantingDataApp;
import ca.gc.tri_agency.granting_data.repo.SystemFundingOpportunityRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GrantingDataApp.class)
@ActiveProfiles("test")
public class SystemFundingOpportunityIntegrationTests {

	@Autowired
	private WebApplicationContext ctx;

	@Autowired
	// SFO data is added through the src/main/resources/db/h2-only-data.xml file
	private SystemFundingOpportunityRepository sfoRepo;

	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(ctx).apply(SecurityMockMvcConfigurers.springSecurity()).build();
	}

	@Test
	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	public void test_unlinkFoBtnVisibleToAdmin_shouldReturn200() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/admin/viewSystemFO").param("id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("id=\"unlinkSfoBtn\"")));
	}

}

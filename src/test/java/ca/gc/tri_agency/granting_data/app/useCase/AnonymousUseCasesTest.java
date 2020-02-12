package ca.gc.tri_agency.granting_data.app.useCase;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ca.gc.tri_agency.granting_data.app.GrantingDataApp;
import ca.gc.tri_agency.granting_data.repo.FundingOpportunityRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GrantingDataApp.class)
@ActiveProfiles("test")
public class AnonymousUseCasesTest {

	@Autowired
	private WebApplicationContext ctx;
	@Autowired
	private FundingOpportunityRepository foRepo;

	private String foId;
	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(ctx).apply(SecurityMockMvcConfigurers.springSecurity()).build();
		foId = Long.toString(foRepo.findAll().get(0).getId());
	}

	@WithAnonymousUser
	@Test
	public void test_anonUserCanViewFoPageInFrench_shouldSucceed200() throws Exception {
		mvc.perform(get("/browse/viewFo").param("id", foId).param("lang", "fr")).andExpect(status().isOk())
				.andExpect(content().string(containsString("id=\"/browse/viewFoPage\"")))
				.andExpect(content().string(containsString("lang=\"fr\""))).andDo(MockMvcResultHandlers.print());
	}

	@WithAnonymousUser
	@Test
	public void test_anonUserCanAccessViewFoPage_shouldSucceedWith200() throws Exception {
		mvc.perform(get("/browse/viewFo").param("id", foId)).andExpect(status().isOk())
				.andExpect(content().string(containsString("id=\"/browse/viewFoPage\"")));
	}

	@WithAnonymousUser
	@Test
	public void test_anonUserCanAccessViewGoldenListPage_shouldSucceedWith200() throws Exception {
		mvc.perform(get("/browse/goldenList")).andExpect(status().isOk())
				.andExpect(content().string(containsString("id=\"/browse/goldenListPage\"")));
	}

}

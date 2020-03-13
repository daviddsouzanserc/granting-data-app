package ca.gc.tri_agency.granting_data.app.useCase;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.regex.Pattern;

import org.hamcrest.Matchers;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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
	public void test_anonUserCanViewFieldsOnViewFoPage_shouldSucceedWith200() throws Exception {
		mvc.perform(get("/browse/viewFo").param("id", foId)).andExpect(status().isOk())
				.andExpect(content().string(containsString("id=\"nameRow\"")))
				.andExpect(content().string(containsString("id=\"leadAgencyRow\"")))
				.andExpect(content().string(containsString("id=\"divisionRow\"")))
				.andExpect(content().string(containsString("id=\"fundingTypeRow\"")))
				.andExpect(content().string(containsString("id=\"programLeadRow\"")));
	}

	@WithAnonymousUser
	@Test
	public void test_anonUserCanViewFoPageInEnglish_shouldSucceed200() throws Exception {
		mvc.perform(get("/browse/viewFo").param("id", foId).param("lang", "en")).andExpect(status().isOk())
				.andExpect(content().string(containsString("Granting Data - View Funding Opportunity")));
	}

	@WithAnonymousUser
	@Test
	public void test_anonUserCanViewFoPageInFrench_shouldSucceed200() throws Exception {
		mvc.perform(get("/browse/viewFo").param("id", foId).param("lang", "fr")).andExpect(status().isOk())
				.andExpect(content().string(containsString("id=\"/browse/viewFoPage\""))).andExpect(content()
						.string(containsString("Octroi de Donn&#233;es - Afficher L'Opportunit&#233; de Financement")));
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

	@WithAnonymousUser
	@Test
	public void test_anonUserCanViewAllFosOnGoldenListPage_shouldSucceedWith200() throws Exception {
		long numFos = foRepo.count();
		String response = mvc.perform(get("/browse/goldenList")).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("id=\"goldenListPage\"")))
				.andReturn().getResponse().getContentAsString();
		Pattern responseRegex = Pattern.compile("<tr>");
		long numRows = responseRegex.splitAsStream(response).filter(str -> str.contains("</tr>")).count();
		// b/c the web page contains 1 table where 1 row contains the table cell headers
		// and every other row contains a FO,
		// the number of FOs should equal the number of rows in the table minus 1
		assertEquals(numFos, numRows - 1L);
	}

}

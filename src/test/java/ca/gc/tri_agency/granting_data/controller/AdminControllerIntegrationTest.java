package ca.gc.tri_agency.granting_data.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ca.gc.tri_agency.granting_data.app.GrantingDataApp;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrantingDataApp.class)
@ActiveProfiles("local")
public class AdminControllerIntegrationTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@WithMockUser(roles = { "MDM ADMIN" })
	@Test
	public void test_onlyAdminCanAddFiscalYears_shouldSucceedWith302() throws Exception {
//		whenever a POST is tried in the Manage FO controller, a 403 forbidden is
//		returned; thus this test fails
		mvc.perform(post("/manage/addFiscalYears").param("year", "2030")).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isFound()).andExpect(MockMvcResultMatchers.redirectedUrl("/browse/viewFiscalYear"));
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	public void test_nonAdminUserCannotAddFundingOpportunities_shouldRedirectToLoginWith302() throws Exception {
//		this only works with HTTP GET and not POST (which is what I think it should be)
		mvc.perform(get("/manage/addFo").param("id", "26").param("nameEn", "A").param("nameFr", "B")
				.param("leadAgency", "3").param("division", "Q").param("isJointIntiative", "false")
				.param("_isJointIntiative", "on").param("partnerOrg", "Z").param("isComplex", "false")
				.param("_isComplex", "on").param("isEdiRequired", "false").param("_isEdiRequired", "on")
				.param("fundingType", "E").param("frequency", "Once").param("applyMethod", "NOLS")
				.param("awardManagementSystem", "SSHERC").param("isNOI", "false").param("_isNOI", "on")
				.param("isLOI", "false").param("_isLOI", "on")).andExpect(status().isOk())
				.andExpect(content().string(containsString("id=\"forbiddenByRoleErrorPage\"")));
	}

	@WithAnonymousUser
	@Test
	public void test_anonUserCannotAddFundingOpportunities_shouldRedirectToLoginWith302() throws Exception {
//		this only works with HTTP GET and not POST (which is what I think it should be)
		mvc.perform(get("/manage/addFo").param("id", "26").param("nameEn", "A").param("nameFr", "B")
				.param("leadAgency", "3").param("division", "Q").param("isJointIntiative", "false")
				.param("_isJointIntiative", "on").param("partnerOrg", "Z").param("isComplex", "false")
				.param("_isComplex", "on").param("isEdiRequired", "false").param("_isEdiRequired", "on")
				.param("fundingType", "E").param("frequency", "Once").param("applyMethod", "NOLS")
				.param("awardManagementSystem", "SSHERC").param("isNOI", "false").param("_isNOI", "on")
				.param("isLOI", "false").param("_isLOI", "on")).andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@WithMockUser(roles = { "MDM ADMIN" })
	@Test
	public void test_onlyAdminCanAddFundingOpportunities_shouldSucceedWith302() throws Exception {
//		this only works with HTTP GET and not a POST (which is what I think it should be)
		mvc.perform(get("/manage/addFo").param("id", "26").param("nameEn", "A").param("nameFr", "B")
				.param("leadAgency", "3").param("division", "Q").param("isJointIntiative", "false")
				.param("_isJointIntiative", "on").param("partnerOrg", "Z").param("isComplex", "false")
				.param("_isComplex", "on").param("isEdiRequired", "false").param("_isEdiRequired", "on")
				.param("fundingType", "E").param("frequency", "Once").param("applyMethod", "NOLS")
				.param("awardManagementSystem", "SSHERC").param("isNOI", "false").param("_isNOI", "on")
				.param("isLOI", "false").param("_isLOI", "on")).andExpect(status().isFound())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/browse/goldenList"));
	}

	@WithAnonymousUser
	@Test
	public void givenAnonymousRequestOnAdminUrl_shouldFailWith301() throws Exception {
		mvc.perform(get("/admin/home").contentType(MediaType.APPLICATION_XHTML_XML))
				.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(username = "nserc-user", roles = { "SSHRC" })
	@Test
	public void givenSshrcRequestOnAdminUrl_shouldFailWithForbiddenByRoleError() throws Exception {
		mvc.perform(get("/admin/home").contentType(MediaType.APPLICATION_XHTML_XML))
				.andExpect(content().string(containsString("id=\"forbiddenByRoleErrorPage\"")));
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void givenAdminAuthRequestOnAdminUrl_shouldSucceedWith200() throws Exception {
		mvc.perform(get("/admin/home").contentType(MediaType.APPLICATION_XHTML_XML)).andExpect(status().isOk());
	}

}

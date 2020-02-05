package ca.gc.tri_agency.granting_data.app.config;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ca.gc.tri_agency.granting_data.app.GrantingDataApp;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrantingDataApp.class)
public class UserRedirectionIntegrationTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@WithAnonymousUser
	@Test
	public void testBadUrlArgs_shouldDirectToManagedErrorPage404() throws Exception {
		String responseStr = mvc.perform(get("/browse/viewFo").param("id", "-999999")).andExpect(status().isNotFound())
				.andReturn().getRequest().getContentAsString();
		assertTrue(responseStr.contains("id=\"generalErrorPage\""));
	}

	@WithAnonymousUser
	@Test
	public void testManageFOControllerAccess_withNonLoggedInUser_shouldRedirectToLogin302() throws Exception {
		mvc.perform(get("/manage/editFo").param("id", "26")).andExpect(status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
	}

	@WithAnonymousUser
	@Test
	public void testAdminControllerAccess_withNonLoggedInUser_shouldRedirectToLogin302() throws Exception {
		mvc.perform(get("/admin/home")).andExpect(status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
		mvc.perform(get("/admin/selectFileForComparison")).andExpect(status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
		mvc.perform(get("/admin/importProgramsFromFile")).andExpect(status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER", "nserc-user-edi" })
	@Test
	public void testAdminControllerAccess_withLoggedInNonAdminUser_shouldBeForbidden() throws Exception {
		String responseString = mvc.perform(get("/admin/home")).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertTrue(responseString.contains("id=\"forbiddenByRoleErrorPage\""));
		responseString = mvc.perform(get("/admin/selectFileForComparison")).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		assertTrue(responseString.contains("id=\"forbiddenByRoleErrorPage\""));
		responseString = mvc.perform(get("/admin/importProgramsFromFile")).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		assertTrue(responseString.contains("id=\"forbiddenByRoleErrorPage\""));
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void testAdminControllerAccess_withAdminUser_shouldSucceed200() throws Exception {
		mvc.perform(get("/admin/home").contentType(MediaType.APPLICATION_XHTML_XML)).andExpect(status().isOk());
		mvc.perform(get("/admin/selectFileForComparison").contentType(MediaType.APPLICATION_XHTML_XML))
				.andExpect(status().isOk());
		mvc.perform(get("/admin/importProgramsFromFile").contentType(MediaType.APPLICATION_XHTML_XML))
				.andExpect(status().isOk());
	}

}

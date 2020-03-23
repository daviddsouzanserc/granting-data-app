package ca.gc.tri_agency.granting_data.app.useCase.manageBusinessUnits;

import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ca.gc.tri_agency.granting_data.app.GrantingDataApp;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrantingDataApp.class)
@ActiveProfiles("test")
public class PBI_19048_CreateBusinessUnitTest {

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
	public void test_editLinkVisibleToAdminOnViewAgency_shouldSucceedWith200() throws Exception {
		mvc.perform(get("/browser/viewAgency?id=1")).andExpect(status().isOk()).andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString("id=\"createBusinessUnit\"")));
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	public void test_editLinkNotVisibleToNonAdminOnViewAgency_shouldSucceedWith200() throws Exception {
		mvc.perform(get("/browser/viewAgency?id=1")).andExpect(status().isOk()).andExpect(
				MockMvcResultMatchers.content().string(not(Matchers.containsString("id=\"createBusinessUnit\""))));
	}

	// CREATE PAGE CAN ONLY BE ACCESSED BY ADMIN
	// CREATE POST ACTION CAN ONLY BE EXECUTED BY ADMIN
	// CREATE ACTION RE-DIRECTS TO VIEW AGENCY PAGE

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void requestSelectBackendFileForComparison_withAdminUser_shouldSucceedWith200() throws Exception {
		mvc.perform(get("/admin/selectFileForComparison").contentType(MediaType.APPLICATION_XHTML_XML))
				.andExpect(status().isOk());
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void testSelectFileForCopmarisonFilePageLinkRequests() throws Exception {
		mvc.perform(get("/admin/analyzeFoUploadData").param("filename", "NAMIS-TestFile.xlsx"))
				.andExpect(status().isOk());
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void testDatabaseIsPopulatedOnInstall() {
		// assertTrue("There are " + foRepo.findAll().size() + " FOs in the db",
		// foRepo.findAll().size() > 100);
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void testChangeProgramLeadLink_visibleWithAdminUser() throws Exception {
//		foId = foRepo.findAll().get(0).getId().toString();
//		mvc.perform(get("/manage/manageFo").param("id", foId)).andExpect(status().isOk()).andExpect(
//				MockMvcResultMatchers.content().string(containsString("href=\"editProgramLead?id=" + foId + "\"")));
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void testViewEditProgramLead_withAdminUser_expectOk() throws Exception {
//		foId = foRepo.findAll().get(0).getId().toString();
//		mvc.perform(get("/manage/editProgramLead").param("id", foId)).andExpect(status().isOk());
	}

}

package ca.gc.tri_agency.granting_data.app.useCase;

import static org.hamcrest.CoreMatchers.containsString;

//import static org.junit.Assert.assertArrayEquals;
//import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;

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
import ca.gc.tri_agency.granting_data.repo.FundingOpportunityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrantingDataApp.class)
@ActiveProfiles("test")
public class AdminUseCasesTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Autowired
	private FundingOpportunityRepository foRepo;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void requestSelectBackendFileForComparison_withAdminUser_shouldSucceedWith200() throws Exception {
		mvc.perform(get("/admin/selectFileForComparison").contentType(MediaType.APPLICATION_XHTML_XML))
				.andExpect(status().isOk());
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void testSelectFileForCopmarisonFilePageLinkRequests() throws Exception {
		mvc.perform(get("/admin/analyzeFoUploadData").param("filename", "NAMIS-TestFile.xlsx")).andExpect(status().isOk());
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void testDatabaseIsPopulatedOnInstall() {
		assertTrue("There are " + foRepo.findAll().size() + " FOs in the db", foRepo.findAll().size() > 100);
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void testChangeProgramLeadLink_visibleWithAdminUser() throws Exception {
		mvc.perform(get("/manage/manageFo").param("id", "26")).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(containsString("href=\"editProgramLead?id=26\"")));
	}
	
	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void testViewEditProgramLead_withAdminUser_expectOk() throws Exception {
		mvc.perform(get("/manage/editProgramLead").param("id", "26")).andExpect(status().isOk());
	}

}

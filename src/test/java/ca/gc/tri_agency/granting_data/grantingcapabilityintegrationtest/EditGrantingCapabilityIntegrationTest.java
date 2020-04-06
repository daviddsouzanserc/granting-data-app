package ca.gc.tri_agency.granting_data.grantingcapabilityintegrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Ignore;
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
import ca.gc.tri_agency.granting_data.service.GrantingCapabilityService;

@SpringBootTest(classes = GrantingDataApp.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
public class EditGrantingCapabilityIntegrationTest {

	@Autowired
	private WebApplicationContext ctx;

	@Autowired
	private GrantingCapabilityService gsService;

	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(ctx).apply(SecurityMockMvcConfigurers.springSecurity()).build();
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void test_editGCLinkVisibleToAdmin_shouldSucceedWith200() throws Exception {
		long numGCs = gsService.findGrantingCapabilitiesByFoId(1L).size();
		assertTrue(numGCs > 0);

		String response = mvc.perform(MockMvcRequestBuilders.get("/manage/manageFo").param("id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		Pattern regex = Pattern.compile("\n");
		long numEditGCLinks = regex.splitAsStream(response).filter(line -> line.contains("href=\"editGC?id=")).count();
		
		assertEquals(numGCs, numEditGCLinks);
	}
	
	@WithMockUser(username = "admin", roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	public void test_editGCLinkNotVisibleToNonAdmin_shouldReturn200() throws Exception {
		long numGCs = gsService.findGrantingCapabilitiesByFoId(1L).size();
		assertTrue(numGCs > 0);

		String response = mvc.perform(MockMvcRequestBuilders.get("/manage/manageFo").param("id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		Pattern regex = Pattern.compile("\n");
		long numEditGCLinks = regex.splitAsStream(response).filter(line -> line.contains("href=\"editGC?id=")).count();
		
		assertNotEquals(numGCs, numEditGCLinks);
		assertEquals(0, numEditGCLinks);
	}

	@Ignore
	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void test_adminCanAccessEditGCPage_shouldSucceedWith200() throws Exception {
		// TODO: COMPLETE DEFINITION
	}

	@Ignore
	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	public void test_nonAdminCannotAccessEditGCPage_shouldReturn403() throws Exception {
		// TODO: COMPLETE DEFINITION
	}

	@Ignore
	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void testService_adminCanEditGC() {
		// TODO: COMPLETE DEFINITION
	}

	@Ignore
	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test(expected = AccessDeniedException.class)
	public void testService_nonAdminCannotEditGC_shouldThrowAccessDeniedException() {
		// TODO: COMPLETE DEFINITION
	}

	@Ignore
	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void testController_adminCanEditGC_shouldSucceedWith302() throws Exception {
		// TODO: COMPLETE DEFINITION
	}

	@Ignore
	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	public void testController_nonAdminCannotEditGC_shouldReturn403() throws Exception {
		// TODO: COMPLETE DEFINITION
	}

}

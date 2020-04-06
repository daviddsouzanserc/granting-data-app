package ca.gc.tri_agency.granting_data.grantingcapabilityintegrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
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
import ca.gc.tri_agency.granting_data.model.GrantingCapability;
import ca.gc.tri_agency.granting_data.repo.GrantingCapabilityRepository;
import ca.gc.tri_agency.granting_data.service.GrantingCapabilityService;

@SpringBootTest(classes = GrantingDataApp.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
public class EditGrantingCapabilityIntegrationTest {

	@Autowired
	private WebApplicationContext ctx;

	@Autowired
	private GrantingCapabilityService gcService;
	@Autowired
	private GrantingCapabilityRepository gcRepo;

	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(ctx).apply(SecurityMockMvcConfigurers.springSecurity()).build();
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void test_editGCLinkVisibleToAdmin_shouldSucceedWith200() throws Exception {
		long numGCs = gcService.findGrantingCapabilitiesByFoId(1L).size();
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
		long numGCs = gcService.findGrantingCapabilitiesByFoId(1L).size();
		assertTrue(numGCs > 0);

		String response = mvc.perform(MockMvcRequestBuilders.get("/manage/manageFo").param("id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		Pattern regex = Pattern.compile("\n");
		long numEditGCLinks = regex.splitAsStream(response).filter(line -> line.contains("href=\"editGC?id=")).count();

		assertNotEquals(numGCs, numEditGCLinks);
		assertEquals(0, numEditGCLinks);
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void testService_adminCanEditGC() {
		long initGcRepoCount = gcRepo.count();

		GrantingCapability gc = gcService.findGrantingCapabilityById(1L);
		String editDescription = "TEST DESCRIPTION EDIT";
		gc.setDescription(editDescription);
		gcService.saveGrantingCapability(gc);

		assertTrue(gcService.findGrantingCapabilityById(1L).getDescription().equals(editDescription));
		assertEquals(initGcRepoCount, gcRepo.count());
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test(expected = AccessDeniedException.class)
	public void testService_nonAdminCannotEditGC_shouldThrowAccessDeniedException() {
		GrantingCapability gc = gcService.findGrantingCapabilityById(1L);
		String editDescription = "TEST DESCRIPTION EDIT";
		gc.setDescription(editDescription);
		gcService.saveGrantingCapability(gc);
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void test_adminCanAccessEditGCPage_shouldSucceedWith200() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/manage/editGC").param("id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content()
						.string(Matchers.containsString("id=\"editGrantingCapabilityPage\"")));
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	public void test_nonAdminCannotAccessEditGCPage_shouldReturn403() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/manage/editGC").param("id", "1"))
				.andExpect(MockMvcResultMatchers.status().isForbidden()).andExpect(MockMvcResultMatchers.content()
						.string(Matchers.containsString("id=\"forbiddenByRoleErrorPage\"")));
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void testController_adminCanEditGC_shouldSucceedWith302() throws Exception {
		long initGcRepoCount = gcRepo.count();
		GrantingCapability gcBeforeUpdate = gcService.findGrantingCapabilityById(1L);

		String description = RandomStringUtils.randomAlphabetic(10);
		String url = "www" + RandomStringUtils.randomAlphabetic(10) + ".ca";
		String foId = "1";
		String gStageId = "1";
		String gSystemId = "1";

		mvc.perform(MockMvcRequestBuilders.post("/manage/editGC").param("id", "1").param("description", description)
				.param("foId", foId).param("gStageId", gStageId).param("gSystemId", gSystemId))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/manage/manageFo?id=" + foId))
				.andExpect(MockMvcResultMatchers.flash().attribute("actionMsg", "Edited a Granting Capability"));

		mvc.perform(MockMvcRequestBuilders.get("/manage/manageFo").param("id", foId))
				.andExpect(MockMvcResultMatchers.flash().attributeCount(0));

		GrantingCapability gcAfterUpdate = gcService.findGrantingCapabilityById(1L);

		assertNotEquals(gcBeforeUpdate, gcAfterUpdate);
		assertEquals(description, gcAfterUpdate.getDescription());
		assertEquals(url, gcAfterUpdate.getUrl());
		assertEquals(foId, gcAfterUpdate.getFundingOpportunity().getId());
		assertEquals(gStageId, gcAfterUpdate.getGrantingStage().getId());
		assertEquals(gSystemId, gcAfterUpdate.getGrantingSystem().getId());

		assertEquals(initGcRepoCount, gcRepo.count());
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	public void testController_nonAdminCannotEditGC_shouldReturn403() throws Exception {
		long initGcRepoCount = gcRepo.count();
		GrantingCapability gcBeforeFailedUpdate = gcService.findGrantingCapabilityById(1L);

		String description = RandomStringUtils.randomAlphabetic(10);
		String url = "www" + RandomStringUtils.randomAlphabetic(10) + ".ca";
		String foId = "1";
		String gStageId = "1";
		String gSystemId = "1";

		mvc.perform(MockMvcRequestBuilders.post("/manage/editGC").param("id", "1").param("description", description)
				.param("foId", foId).param("gStageId", gStageId).param("gSystemId", gSystemId))
				.andExpect(MockMvcResultMatchers.status().isForbidden()).andExpect(MockMvcResultMatchers.content()
						.string(Matchers.containsString("id=\"forbiddenByRoleErrorPage\"")));

		GrantingCapability gcAfterFailedUpdate = gcService.findGrantingCapabilityById(1L);

		assertEquals(gcBeforeFailedUpdate, gcAfterFailedUpdate);
		assertNotEquals(description, gcAfterFailedUpdate.getDescription());
		assertNotEquals(url, gcAfterFailedUpdate.getUrl());
		assertNotEquals(foId, gcAfterFailedUpdate.getFundingOpportunity().getId());
		assertNotEquals(gStageId, gcAfterFailedUpdate.getGrantingStage().getId());
		assertNotEquals(gSystemId, gcAfterFailedUpdate.getGrantingSystem().getId());

		assertEquals(initGcRepoCount, gcRepo.count());
	}

}

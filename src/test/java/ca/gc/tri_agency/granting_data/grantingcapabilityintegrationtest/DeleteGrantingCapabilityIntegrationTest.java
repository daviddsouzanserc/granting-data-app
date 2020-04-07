package ca.gc.tri_agency.granting_data.grantingcapabilityintegrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataRetrievalFailureException;
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
import ca.gc.tri_agency.granting_data.repo.GrantingCapabilityRepository;
import ca.gc.tri_agency.granting_data.service.GrantingCapabilityService;

@SpringBootTest(classes = GrantingDataApp.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
public class DeleteGrantingCapabilityIntegrationTest {

	@Autowired
	private GrantingCapabilityService gcService;

	@Autowired
	private GrantingCapabilityRepository gcRepo;

	@Autowired
	private WebApplicationContext ctx;

	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(ctx).apply(SecurityMockMvcConfigurers.springSecurity()).build();
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void test_deleteGCLinkVisibleToAdmin_shouldSucceedWith200() throws Exception {
		long numGCs = gcService.findGrantingCapabilitiesByFoId(1L).size();
		assertTrue(numGCs > 0);

		String response = mvc.perform(MockMvcRequestBuilders.get("/manage/manageFo").param("id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		Pattern regex = Pattern.compile("\n");
		long numDeleteGCLinks = regex.splitAsStream(response)
				.filter(line -> line.contains("id=\"deleteGrantingCapabilityLink\"")).count();

		assertEquals(numGCs, numDeleteGCLinks);
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	public void test_deleteGCLinkNotVisibleToNonAdmin_shouldReturn200() throws Exception {
		long numGCs = gcService.findGrantingCapabilitiesByFoId(1L).size();
		assertTrue(numGCs > 0);

		String response = mvc.perform(MockMvcRequestBuilders.get("/manage/manageFo").param("id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		Pattern regex = Pattern.compile("\n");
		long numDeleteGCLinks = regex.splitAsStream(response)
				.filter(line -> line.contains("id=\"deleteGrantingCapabilityLink\"")).count();

		assertNotEquals(numGCs, numDeleteGCLinks);
		assertEquals(0L, numDeleteGCLinks);
	}

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test(expected = DataRetrievalFailureException.class)
	public void testService_adminCanDeleteGC() {
		long numGCs = gcRepo.count();

		gcService.deleteGrantingCapability(100L);

		assertEquals(numGCs - 1, gcRepo.count());

		gcService.findGrantingCapabilityById(100L);
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test(expected = AccessDeniedException.class)
	public void testService_nonAdminCannotDeleteGC_shouldThrowAccessDeniedExcepction() {
		gcService.deleteGrantingCapability(100L);
	}
}

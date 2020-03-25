package ca.gc.tri_agency.granting_data.app.useCase.manageBusinessUnits;

import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
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
		mvc.perform(get("/browse/viewAgency?id=1")).andExpect(status().isOk()).andExpect(
				MockMvcResultMatchers.content().string(Matchers.containsString("id=\"createBusinessUnit\"")));
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	public void test_editLinkNotVisibleToNonAdminOnViewAgency_shouldSucceedWith200() throws Exception {
		mvc.perform(get("/browse/viewAgency?id=1")).andExpect(status().isOk()).andExpect(
				MockMvcResultMatchers.content().string(not(Matchers.containsString("id=\"createBusinessUnit\""))));
	}

	//
	// CREATE PAGE CAN ONLY BE ACCESSED BY ADMIN
	// TODO:: add expect ref admin to be selected in Agency Input
	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void test_accessCreatePageByAdmin_shouldSucceedWith200() throws Exception {
		mvc.perform(get("/admin/createBusinessUnit?agencyId=1")).andExpect(status().isOk());
	}

	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	@Test
	public void test_accessCreateBusinessUnitByNonAdmin_shouldReturn403() throws Exception {
		mvc.perform(get("/admin/createBusinessUnit?agencyId=1")).andExpect(status().isOk())
				.andExpect(status().isForbidden()).andReturn().getResponse().getContentAsString()
				.contains("id=\"forbiddenByRoleErrorPage\"");
	}

	@Test
	@Transactional(readOnly = true)
	@WithMockUser(roles = { "NSERC_USER", "SSHRC_USER", "AGENCY_USER" })
	public void test_unlinkFoConfirmationPageNotAccessableByNonAdminUsers_shouldReturn403() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/admin/confirmUnlink").param("sfoId", "1"))
				.andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn().getResponse().getContentAsString()
				.contains("id=\"forbiddenByRoleErrorPage\"");
	}

	// CREATE POST ACTION CAN ONLY BE EXECUTED BY ADMIN
	@WithMockUser(roles = { "MDM ADMIN" })
	@Test
	public void test_onlyAdminCanAddFundingOpportunities_shouldSucceedWith302() throws Exception {

		mvc.perform(post("/admin/createBusinessUnit").param("agencyId", "1").param("nameEn", "A").param("nameFr", "B")
				.param("acronymleadAgency", "3").param("division", "Q").param("isJointIntiative", "false")
				.param("_isJointIntiative", "on").param("partnerOrg", "Z").param("isComplex", "false")
				.param("_isComplex", "on").param("isEdiRequired", "false").param("_isEdiRequired", "on")
				.param("fundingType", "E").param("frequency", "Once").param("applyMethod", "NOLS")
				.param("awardManagementSystem", "SSHERC").param("isNOI", "false").param("_isNOI", "on")
				.param("isLOI", "false").param("_isLOI", "on")).andExpect(status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/browse/goldenList"));

	}

	// CREATE ACTION RE-DIRECTS TO VIEW AGENCY PAGE

}

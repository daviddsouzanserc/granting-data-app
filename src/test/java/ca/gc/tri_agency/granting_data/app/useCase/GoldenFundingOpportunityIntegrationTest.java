package ca.gc.tri_agency.granting_data.app.useCase;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;

import ca.gc.tri_agency.granting_data.app.GrantingDataApp;
import ca.gc.tri_agency.granting_data.controller.AdminController;
import ca.gc.tri_agency.granting_data.controller.ManageFundingOpportunityController;
import ca.gc.tri_agency.granting_data.model.Agency;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.repo.AgencyRepository;
import ca.gc.tri_agency.granting_data.repo.FundingOpportunityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrantingDataApp.class)
@ActiveProfiles("local")
public class GoldenFundingOpportunityIntegrationTest {

	@Autowired
	private AdminController adminController;
	@Autowired
	private AgencyRepository agencyRepo;
	@Autowired
	private FundingOpportunityRepository foRepo;
	@Autowired
	private ManageFundingOpportunityController mFoController;

	@Mock
	BindingResult bindingResult;
	@Mock
	Model model;

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void test_AdminCanCreateGoldenFo_shouldSucceed() throws Exception {
		long initFoRepoSize = foRepo.count();
		
		FundingOpportunity gfo = new FundingOpportunity();
		gfo.setApplyMethod(RandomStringUtils.randomAlphabetic(10));
		gfo.setAwardManagementSystem(RandomStringUtils.randomAlphabetic(10));
		gfo.setComplex(false);
		gfo.setDivision(RandomStringUtils.randomAlphabetic(10));
		gfo.setEdiRequired(false);
		gfo.setFrequency(RandomStringUtils.randomAlphabetic(10));
		gfo.setFundingType(RandomStringUtils.randomAlphabetic(10));
		gfo.setIsLOI(false);
		gfo.setIsNOI(false);
		gfo.setJointInitiative(false);
		gfo.setNameEn(RandomStringUtils.randomAlphabetic(25));
		gfo.setNameFr(RandomStringUtils.randomAlphabetic(25));
		gfo.setPartnerOrg(RandomStringUtils.randomAlphabetic(25));
		gfo.setProgramLeadDn(RandomStringUtils.randomAlphabetic(25));
		gfo.setProgramLeadName(RandomStringUtils.randomAlphabetic(25));
		List<Agency> agencyList = agencyRepo.findAll();
		gfo.setLeadAgency(agencyList.size() > 0 ? agencyList.remove(0) : null);
		gfo.setParticipatingAgencies(agencyList.size() > 0 ? new HashSet<Agency>(agencyList) : null);

		String successUrl = mFoController.addFoPost(gfo, bindingResult, model);

		assertEquals("redirect:/browse/goldenList", successUrl);
		assertEquals(initFoRepoSize + 1, foRepo.count());
	}

}

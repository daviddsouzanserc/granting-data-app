package ca.gc.tri_agency.granting_data.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.gc.tri_agency.granting_data.app.GrantingDataApp;
import ca.gc.tri_agency.granting_data.model.GrantingCapability;
import ca.gc.tri_agency.granting_data.repo.FundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.repo.GrantingCapabilityRepository;
import ca.gc.tri_agency.granting_data.repo.GrantingStageRepository;
import ca.gc.tri_agency.granting_data.repo.GrantingSystemRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GrantingDataApp.class)
@ActiveProfiles("test")
public class RestrictedDataServiceIntegrationTest {

	@Autowired
	private RestrictedDataService restrictedDataService;
	@Autowired
	private GrantingCapabilityRepository gcRepo;
	@Autowired
	private GrantingSystemRepository gSystemRepo;
	@Autowired
	private GrantingStageRepository gStageRepo;
	@Autowired
	private FundingOpportunityRepository foRepo;

	@WithMockUser(username = "admin", roles = { "MDM ADMIN" })
	@Test
	public void test_createGrantingCapabaility() {
		GrantingCapability newGc = new GrantingCapability();
		newGc.setDescription("TEST GRANTING CAPABILITY");
		newGc.setUrl("www.testGrantingCapability.com");
		newGc.setGrantingStage(gStageRepo.findAll().get(0));
		newGc.setGrantingSystem(gSystemRepo.findAll().get(0));
		newGc.setFundingOpportunity(foRepo.findAll().get(0));

		GrantingCapability addedGc = restrictedDataService.createGrantingCapability(newGc);

		assertNotNull(addedGc);

		Long addedGcId = addedGc.getId();
		assertEquals(gcRepo.findById(addedGcId).get().getFundingOpportunity().getNameEn(),
				newGc.getFundingOpportunity().getNameEn());
		assertEquals(gcRepo.findById(addedGcId).get().getDescription(), newGc.getDescription());
	}

}

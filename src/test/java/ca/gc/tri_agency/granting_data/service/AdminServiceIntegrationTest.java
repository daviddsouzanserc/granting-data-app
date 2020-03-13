package ca.gc.tri_agency.granting_data.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import ca.gc.tri_agency.granting_data.app.GrantingDataApp;
import ca.gc.tri_agency.granting_data.model.GrantingSystem;
import ca.gc.tri_agency.granting_data.model.SystemFundingOpportunity;
import ca.gc.tri_agency.granting_data.model.file.FundingCycleDatasetRow;
import ca.gc.tri_agency.granting_data.repo.FundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.repo.GrantingSystemRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingCycleRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.security.annotations.AdminOnly;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GrantingDataApp.class)
@ActiveProfiles("test")
public class AdminServiceIntegrationTest {

	@Autowired
	AdminService adminService;
	@Autowired
	SystemFundingCycleRepository sfcRepo;
	@Autowired
	SystemFundingOpportunityRepository sfoRepo;
	@Autowired
	GrantingSystemRepository gsRepo;
	@Autowired
	FundingOpportunityRepository foRepo;
	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	static String testFoName = "TESTFO";

	private static final String TEST_FILE = "NAMIS-TestFile.xlsx";

	/* TEST UTIL FUNCTION */
	FundingCycleDatasetRow createFcDatasetRow(String foName, String year) {
		FundingCycleDatasetRow retval = new FundingCycleDatasetRow();
		retval.setCompetitionYear(2019L);
		retval.setFoCycle(foName + "-" + year);
		retval.setProgram_ID(foName);
		retval.setProgramNameEn(foName + "-en");
		retval.setProgramNameFr(foName + "-fr");
		retval.setNumReceivedApps(100);
		return retval;
	}

	@Test
	@WithAnonymousUser
	/*
	 * Changed this method because the application currently has the security setup
	 * so that when an unauthorized user tries to access any page that requires
	 * authentication, that unauthorized user is redirected to the login page.
	 */
	public void test_applyChangesFromFileByIds_unauthorizedUserShouldRedirectToLoginPage() throws Exception {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("fileName", TEST_FILE);
		String idToAction = adminService.getFundingCyclesFromFile(TEST_FILE).get(0).getFoCycle();
		paramMap.add("idToAction", idToAction);
		mvc.perform(post("/admin/analyzeFoUploadData").queryParams(paramMap)).andExpect(status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
	}

	@Test
	@AdminOnly
	@Transactional
	public void test_applyChangesFromFileByIds_regsiterSFCwhenSFOalreadyExists() {
		String targetYear = "2009";
		String sfoName = "SAMPLE";

		/*
		 * A blank SFO is being persisted so that we can obtain a base id value. In
		 * other words, the SFO that will be added with the applyChangesFromFileByIds
		 * method should have an id equal to the id of the blank SFO + 1. Also, Since
		 * the new SFO is persisted in the applyChangesFromFileByIds method, we can't
		 * extract its id until after that invocation.
		 */
		sfoRepo.save(new SystemFundingOpportunity());

		long idForLastSfo = sfoRepo.findAll().get((int) sfoRepo.count() - 1).getId();
		sfcRepo.findAll().forEach(sfo0 -> System.out.println(sfo0.getId()));

		int origCountOfSFCsLinkedToSFO = sfcRepo
				.findByExtIdAndSystemFundingOpportunityId(sfoName + "-" + targetYear, idForLastSfo + 1).size();

		// ADD A CYCLE TO "SAMPLE" thought the service
		final String testFileName = "NAMIS-TestCase_registerSFCwhenSFOalreadyExists.xlsx";
		adminService.applyChangesFromFileByIds(testFileName, new String[] { sfoName + "-" + targetYear });

		int newCountOfSFCsLinkedToSFO = sfcRepo
				.findByExtIdAndSystemFundingOpportunityId(sfoName + "-" + targetYear, idForLastSfo + 1).size();

		/*
		 * This assertTrue and the first assertEquals are added b/c we want to ensure
		 * that origCountOfSFCsLinkedToSFO uses the same SFO extId and SFO id as
		 * newCountOfSFCsLinkedToSFO.
		 */
		int lastIdx = (int) sfoRepo.count() - 1;
		SystemFundingOpportunity addedSfo = sfoRepo.findAll().get(lastIdx);
		assertTrue(addedSfo.getExtId().equals(sfoName + "-" + targetYear));
		assertEquals(addedSfo.getId(), idForLastSfo + 1);

		assertEquals(origCountOfSFCsLinkedToSFO + 1, newCountOfSFCsLinkedToSFO);
	}

	@Test
	@AdminOnly
	public void test_applyChangesFromFileByIds_adminCanRegisterSFOandSFC() throws Exception {
		List<FundingCycleDatasetRow> fundingCycles = adminService.getFundingCyclesFromFile(TEST_FILE);
		String[] idsToAction = new String[] { fundingCycles.get(0).getFoCycle() };
		assertEquals(1, adminService.applyChangesFromFileByIds(TEST_FILE, idsToAction));
	}

	@Test
	public void test_getDatasetFiles_expectNonEmptyList() {
		List<File> myFiles = adminService.getDatasetFiles();
		assertNotEquals(myFiles.size(), 0, "expected non empty list from dataset folder");

	}

	@Test
	public void test_getFundingCyclesFromFile_expect24Rows() {
		List<FundingCycleDatasetRow> rows = adminService.getFundingCyclesFromFile("NAMIS-TestFileBase1.xlsx");
		assertEquals(rows.size(), 24, "expected 24 rows from TestFileBase1.xlsx");
	}

	@Test
	public void test_generateActionableFoCycleIds_actionDueToNewSfoAndSfc() {
		List<FundingCycleDatasetRow> analyzeRows = new ArrayList<FundingCycleDatasetRow>();
		analyzeRows.add(createFcDatasetRow(testFoName + "1", "2019"));
		List<String> result = adminService.generateActionableFoCycleIds(analyzeRows);
		assertEquals(1, result.size(), "expected one action row due to SFC and SFO not created");

	}

	@Test
	public void test_generateActionableFoCycleIds_actionDueToNewSfc() {
		// register FO
		GrantingSystem gs = gsRepo.findAll().get(0);
		FundingCycleDatasetRow testRow = createFcDatasetRow(testFoName + "2", "2019");
		adminService.registerSystemFundingOpportunity(testRow, gs);

		// test
		List<FundingCycleDatasetRow> analyzeRows = new ArrayList<FundingCycleDatasetRow>();
		analyzeRows.add(testRow);
		List<String> result = adminService.generateActionableFoCycleIds(analyzeRows);
		assertEquals(1, result.size(), "expected one action row due to new SFC");

	}

	@Test
	public void test_generateActionableFoCycleIds_noActionCauseSfcAndSfoExists() {
		GrantingSystem gs = gsRepo.findAll().get(0);
		FundingCycleDatasetRow testRow = createFcDatasetRow(testFoName + "3", "2019");
		SystemFundingOpportunity sfo = adminService.registerSystemFundingOpportunity(testRow, gs);
		adminService.registerSystemFundingCycle(testRow, sfo);

		List<FundingCycleDatasetRow> analyzeRows = new ArrayList<FundingCycleDatasetRow>();
		analyzeRows.add(createFcDatasetRow(testFoName + "3", "2019"));
		List<String> result = adminService.generateActionableFoCycleIds(analyzeRows);
		assertEquals(0, result.size(), "expected zero action rows as data exists");

	}

}

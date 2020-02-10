package ca.gc.tri_agency.granting_data.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import ca.gc.tri_agency.granting_data.app.GrantingDataApp;
import ca.gc.tri_agency.granting_data.model.GrantingSystem;
import ca.gc.tri_agency.granting_data.model.SystemFundingCycle;
import ca.gc.tri_agency.granting_data.model.SystemFundingOpportunity;
import ca.gc.tri_agency.granting_data.model.file.FundingCycleDatasetRow;
import ca.gc.tri_agency.granting_data.repo.FundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.repo.GrantingSystemRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingCycleRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.security.annotations.AdminOnly;
import ca.gc.tri_agency.granting_data.service.impl.AdminServiceImpl;

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
	@Autowired
	private AdminServiceImpl adminServiceImpl;

	private MockMvc mvc;

	static String testFoName = "TESTFO";

	private static final String TEST_FILE = "NAMIS-TestFile.xlsx";

	/* TEST UTIL FUNCTION */
	FundingCycleDatasetRow createFcDatasetRow(String foName, String year) {
		FundingCycleDatasetRow retval = new FundingCycleDatasetRow();
		retval.setCompetitionYear(Long.getLong("2019"));
		retval.setFoCycle(foName + "-" + year);
		retval.setProgram_ID(foName);
		retval.setProgramNameEn(foName + "-en");
		retval.setProgramNameFr(foName + "-fr");
		retval.setNumReceivedApps(100);
		return retval;
	}

	@Test
	@WithAnonymousUser
	public void test_applyChangesFromFileByIds_unauthorizedUserShouldReturn401() throws Exception {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("fileName", TEST_FILE);
		String idToAction = adminService.getFundingCyclesFromFile(TEST_FILE).get(0).getFoCycle();
		paramMap.add("idToAction", idToAction);
		mvc.perform(post("/admin/analyzeFoUploadData").queryParams(paramMap)).andExpect(status().isUnauthorized());
	}

	@Test
	@AdminOnly
	@Transactional
	public void test_applyChangesFromFileByIds_regsiterSFCwhenSFOalreadyExists() {
		String targetYear = "2009";
		String sfoName = "SAMPLE";

		long initSfcTableSize = sfcRepo.count();
		long initSfoTableSize = sfoRepo.count();

		final String testFileName = "NAMIS-TestCase_registerSFCwhenSFOalreadyExists.xlsx";
		adminService.applyChangesFromFileByIds(testFileName, new String[] { sfoName + "-" + targetYear });

		long newSizeSfoRepo = sfoRepo.count();

		// check that SFO was added to the DB
		assertEquals(initSfoTableSize + 1, newSizeSfoRepo);

		// check that newly added SFO does not have a SFC
		final SystemFundingOpportunity newSfo = sfoRepo.getOne(newSizeSfoRepo);
		assertNull(newSfo.getExtId());

		// register SFC with newly added SFO
		List<FundingCycleDatasetRow> fcRows = adminService.getFundingCyclesFromFile(testFileName);
		final SystemFundingCycle sfc = adminService.registerSystemFundingCycle(fcRows.get(0), newSfo);

		/*
		 * Check that newly added SFO now has a SFC however, there is no
		 * getSystemFundingCycle() method for the SystemFundingOpportunity class even
		 * though there is a getSystemFundingOpportunity() method in the
		 * SystemFundingCycle class. When getExtId() is used, the problem is that value
		 * is already in the Excel file. If the ext_id value is removed from the Excel
		 * file, the assertion below will fail b/c there is no functionality in
		 * registerSystemFundingCycle() that will add it. If it is added then the
		 * assertion on line 110 will fail.
		 */
		assertNotNull(newSfo.getExtId());

		// check that SFC table has a new entry
		assertEquals(initSfcTableSize + 1, sfcRepo.count());

		// I think the assertion below would be better if there is a
		// getSystemFundingOpportunity() method
		assertEquals(sfc.getExtId(), newSfo.getExtId());
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

package ca.gc.tri_agency.granting_data.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.gc.tri_agency.granting_data.app.GrantingDataApp;
import ca.gc.tri_agency.granting_data.model.GrantingSystem;
import ca.gc.tri_agency.granting_data.model.SystemFundingOpportunity;
import ca.gc.tri_agency.granting_data.model.file.FundingCycleDatasetRow;
import ca.gc.tri_agency.granting_data.repo.GrantingSystemRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingCycleRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingOpportunityRepository;

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

	static String testFoName = "TESTFO";

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

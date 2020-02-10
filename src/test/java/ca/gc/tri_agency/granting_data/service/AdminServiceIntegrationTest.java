package ca.gc.tri_agency.granting_data.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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

	private static final String TEST_FILE = "NAMIS-TestFileBase1.xlsx";

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
	public void test__applyChangesFromFileByIds_unauthorizedUserShouldReturn401() throws Exception {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("fileName", TEST_FILE);
		paramMap.add("idToAction", "AANSE-2014");
		mvc.perform(post("/admin/analyzeFoUploadData").queryParams(paramMap)).andExpect(status().isUnauthorized());
	}

	@Test
	public void test_regsiterSFCwhenSFOalreadyExists() {
		SystemFundingOpportunity sfo = new SystemFundingOpportunity();

		// sfo.setExtId(RandomStringUtils.randomAlphabetic(10));

		sfo.setLinkedFundingOpportunity(foRepo.findById(26L).get());
		sfo.setGrantingSystem(gsRepo.findById(25L).get());
		sfo.setNameEn(RandomStringUtils.randomAlphabetic(10));
		sfo.setNameFr(RandomStringUtils.randomAlphabetic(10));
		sfoRepo.save(sfo);

		sfoRepo.findAll().forEach(sysFo -> System.out.printf(
				"%nID = %d%nExtension ID = %s%nGranting System = %s%nLinked FO Name = %s%nEnglish Name = %s%nFrench Name = %s%n%n",
				sysFo.getId(), sysFo.getExtId(), sysFo.getGrantingSystem().getNameEn(),
				sysFo.getLinkedFundingOpportunity().getNameEn(), sysFo.getNameEn(), sysFo.getNameFr()));

		// invoked a method that creates the corresponding SFC

		// assert that the sfo in the database has a reference to the corresponding SFC
		// that was just created
	}

	@Test
	public void test_applyChangesFromFileByIs_registerSFOandSFC() throws Exception {
		assertEquals(2, adminService.applyChangesFromFileByIds(TEST_FILE, new String[] { "AANSE-2009", "BCPIR-2004" }));
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

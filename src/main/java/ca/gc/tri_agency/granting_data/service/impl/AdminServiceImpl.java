package ca.gc.tri_agency.granting_data.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;

import ca.gc.tri_agency.granting_data.model.BusinessUnit;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.model.GrantingSystem;
import ca.gc.tri_agency.granting_data.model.SystemFundingCycle;
import ca.gc.tri_agency.granting_data.model.SystemFundingOpportunity;
import ca.gc.tri_agency.granting_data.model.file.FundingCycleDatasetRow;
import ca.gc.tri_agency.granting_data.repo.AgencyRepository;
import ca.gc.tri_agency.granting_data.repo.BusinessUnitRepository;
import ca.gc.tri_agency.granting_data.repo.FundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.repo.GrantingSystemRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingCycleRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.security.annotations.AdminOnly;
import ca.gc.tri_agency.granting_data.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	/** Logger */
	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	private SystemFundingOpportunityRepository systemFoRepo;

	@Autowired
	private FundingOpportunityRepository foRepo;

	@Autowired
	private AgencyRepository agencyRepo;

	@Autowired
	private SystemFundingCycleRepository systemFundingCycleRepo;

	@Autowired
	private GrantingSystemRepository grantingSystemRepo;

	@Autowired
	private BusinessUnitRepository buRepo;

	@Value("${dataset.analysis.folder}")
	private String datasetAnalysisFolder;

	@Override
	public List<File> getDatasetFiles() {
		Path dir = Paths.get(datasetAnalysisFolder);
		List<File> list = new ArrayList<File>();
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir)) {
			dirStream.forEach(path -> list.add(path.toFile()));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return list;
	}

	@Override
	public List<FundingCycleDatasetRow> getFundingCyclesFromFile(String filename) {
		Collection<FundingCycleDatasetRow> rows = null;
		Xcelite xcelite;
		xcelite = new Xcelite(new File(datasetAnalysisFolder + filename));
		XceliteSheet sheet = xcelite.getSheet(0);
		SheetReader<FundingCycleDatasetRow> reader = sheet.getBeanReader(FundingCycleDatasetRow.class);
		rows = reader.read();

		return new ArrayList<FundingCycleDatasetRow>(rows);
	}

	@Override
	public List<String> generateActionableFoCycleIds(List<FundingCycleDatasetRow> foCycles) {
		// SYSTEM FCs HAVE UNIQUE IDENTIFIER THAT INCLUDES THE PROGRAM IDENTIFIER. USING
		// THAT AS DETERMINATION FACTOR
		List<SystemFundingCycle> dbFundingCycles = systemFundingCycleRepo.findAll();
		List<String> retval = new ArrayList<String>();
		for (FundingCycleDatasetRow row : foCycles) {
			boolean rowFound = false;
			for (SystemFundingCycle cycle : dbFundingCycles) {
				if (cycle.getExtId().compareTo(row.getFoCycle()) == 0) {
					rowFound = true;
				}
			}
			if (rowFound == false) {
				retval.add(row.getFoCycle());
			}
		}
		return retval;
	}

	@Override
	public SystemFundingOpportunity registerSystemFundingOpportunity(FundingCycleDatasetRow row,
			GrantingSystem targetSystem) {
		SystemFundingOpportunity retval = new SystemFundingOpportunity();
		retval.setExtId(row.getFoCycle());
		retval.setNameEn(row.getProgramNameEn());
		retval.setNameFr(row.getProgramNameFr());
		retval.setGrantingSystem(targetSystem);
		retval = systemFoRepo.save(retval);
		return retval;

	}

	@Override
	public SystemFundingCycle registerSystemFundingCycle(FundingCycleDatasetRow row,
			SystemFundingOpportunity targetSfo) {
		SystemFundingCycle retval = new SystemFundingCycle();
		retval.setFiscalYear(row.getCompetitionYear());
		retval.setExtId(row.getFoCycle());
		retval.setSystemFundingOpportunity(targetSfo);
		retval.setNumAppsReceived(row.getNumReceivedApps());
		retval = systemFundingCycleRepo.save(retval);
		return retval;
	}

	@Override
	public int applyChangesFromFileByIds(String filename, String[] idsToAction) {
		GrantingSystem targetSystem = getGrantingSystemFromFilename(filename);

		// transform into list for inspection capabilities
		List<String> actionList = Arrays.asList(idsToAction);

		// generate map for lookup
		List<SystemFundingOpportunity> foList = systemFoRepo.findAll();
		HashMap<String, SystemFundingOpportunity> map = new HashMap<String, SystemFundingOpportunity>();
		for (SystemFundingOpportunity fo : foList) {
			map.put(fo.getExtId(), fo);
		}
		// Set<String> foIdList = map.keySet();
		List<FundingCycleDatasetRow> foCyclesList = getFundingCyclesFromFile(filename);
		List<Long> newIdList = new ArrayList<Long>();
		for (FundingCycleDatasetRow row : foCyclesList) {
			if (actionList.contains(row.getFoCycle())) {
				SystemFundingOpportunity targetFo = null;
				// if program exists, use it, else create it
				// String targetId = row.getProgram_ID().substring(0,
				// row.getProgram_ID().length() - 5);
				if (map.containsKey(row.getProgram_ID())) {
					targetFo = map.get(row.getProgram_ID());
				} else {
					targetFo = registerSystemFundingOpportunity(row, targetSystem);
					map.put(row.getProgram_ID(), targetFo);

				}
				SystemFundingCycle newCycle = registerSystemFundingCycle(row, targetFo);
				newIdList.add(newCycle.getId());
			}

		}
		return newIdList.size();

	}

	public GrantingSystem getGrantingSystemFromFilename(String filename) {
		List<GrantingSystem> grantingSystems = grantingSystemRepo.findAll();
		GrantingSystem retval = null;
		for (GrantingSystem system : grantingSystems) {
			if (filename.contains(system.getAcronym())) {
				retval = system;
			}
		}
		return retval;
	}

	@Override
	public int unlinkSystemFO(long systemFoId, long foId) {
		SystemFundingOpportunity systemFo = systemFoRepo.findById(systemFoId)
				.orElseThrow(() -> new DataRetrievalFailureException("That System Funding Opportunity does not exist"));
		FundingOpportunity fo = foRepo.findById(foId)
				.orElseThrow(() -> new DataRetrievalFailureException("That Funding Opportunity does not exist"));
		if (systemFo.getLinkedFundingOpportunity() != fo) {
			throw new DataRetrievalFailureException(
					"System Funding Opportunity is not linked with that Funding Opportunity");
		}
		systemFo.setLinkedFundingOpportunity(null);
		systemFoRepo.save(systemFo);
		return 1;
	}

	@Override
	public int linkSystemFO(long systemFoId, long foId) {
		SystemFundingOpportunity systemFo = systemFoRepo.findById(systemFoId)
				.orElseThrow(() -> new DataRetrievalFailureException("That System Funding Opportunity does not exist"));
		FundingOpportunity fo = foRepo.findById(foId)
				.orElseThrow(() -> new DataRetrievalFailureException("That Funding Opportunity does not exist"));
		systemFo.setLinkedFundingOpportunity(fo);
		systemFoRepo.save(systemFo);
		return 1;
	}

	@Override
	@AdminOnly
	@Transactional
	public BusinessUnit createOrUpdateBusinessUnit(BusinessUnit bu) {
		return buRepo.save(bu);
	}

}

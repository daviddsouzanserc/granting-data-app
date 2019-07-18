package ca.gc.tri_agency.granting_data.service.impl;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;

import ca.gc.tri_agency.granting_data.model.GrantingSystem;
import ca.gc.tri_agency.granting_data.model.SystemFundingCycle;
import ca.gc.tri_agency.granting_data.model.SystemFundingOpportunity;
import ca.gc.tri_agency.granting_data.model.file.FundingCycleDatasetRow;
import ca.gc.tri_agency.granting_data.repo.GrantingSystemRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingCycleRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	/** Logger */
	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	SystemFundingOpportunityRepository systemFoRepo;

	@Autowired
	SystemFundingCycleRepository systemFundingCycleRepo;

	@Autowired
	GrantingSystemRepository grantingSystemRepo;

	@Override
	public List<File> getDatasetFiles() {
		String datasetsFolder = "datasets";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(datasetsFolder).getFile());
		List<File> list = new ArrayList<File>();
		for (File l : file.listFiles()) {
			list.add(l);
		}
		return list;
	}

	@Override
	public List<FundingCycleDatasetRow> getFundingCyclesFromFile(String filename) {
		List<FundingCycleDatasetRow> fileRows = loadObjectList(filename);
		return fileRows;

		// List<FundingCycleDatasetRow> retval = new
		// ArrayList<FundingCycleDatasetRow>();
		// List<SystemFundingCycle> dbFundingCycles =
		// systemFundingCycleRepo.findAll();
		// for (FundingCycleDatasetRow row : fileRows) {
		// boolean found = false;
		// for (SystemFundingCycle cycle : dbFundingCycles) {
		// if (cycle.getExtId().compareTo(row.getFoCycle()) == 0) {
		// found = true;
		// }
		// }
		// if (found == false) {
		// retval.add(row);
		// }
		// }
		// return new ArrayList<FundingCycleDatasetRow>(fileRows);
	}

	private List<FundingCycleDatasetRow> loadObjectList(String fileName) {
		Collection<FundingCycleDatasetRow> rows = null;

		Xcelite xcelite;
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("datasets/" + fileName).getFile());
		xcelite = new Xcelite(file);
		XceliteSheet sheet = xcelite.getSheet(0);
		SheetReader<FundingCycleDatasetRow> reader = sheet.getBeanReader(FundingCycleDatasetRow.class);
		rows = reader.read();

		return new ArrayList<FundingCycleDatasetRow>(rows);

	}

	@Override
	public List<String> generateActionableFoCycleIds(List<FundingCycleDatasetRow> foCycles) {
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
		Set<String> foIdList = map.keySet();
		List<FundingCycleDatasetRow> foCyclesList = getFundingCyclesFromFile(filename);
		List<Long> newIdList = new ArrayList<Long>();
		for (FundingCycleDatasetRow row : foCyclesList) {
			if (actionList.contains(row.getFoCycle())) {
				SystemFundingOpportunity targetFo = null;
				// if program exists, use it, else create it
				if (foIdList.contains(row.getProgram_ID())) {
					targetFo = map.get(row.getProgram_ID());
				} else {
					targetFo = new SystemFundingOpportunity();
					targetFo.setExtId(row.getProgram_ID());
					targetFo.setNameEn(row.getProgramNameEn());
					targetFo.setNameFr(row.getProgramNameFr());
					targetFo.setGrantingSystem(targetSystem);
					targetFo = systemFoRepo.save(targetFo);

				}
				SystemFundingCycle newCycle = new SystemFundingCycle();
				try {
					newCycle.setCompYear(new SimpleDateFormat("yyyy").parse(row.getCompetition_Year()));
				} catch (ParseException e) {
					LOG.log(Level.WARN, "Invalid year:" + row.getCompetition_Year());
				}
				newCycle.setExtId(row.getFoCycle());
				newCycle.setSystemFundingOpportunity(targetFo);
				newCycle.setNumAppsReceived(row.getNumReceivedApps());
				newCycle = systemFundingCycleRepo.save(newCycle);
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

}

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
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;

import ca.gc.tri_agency.granting_data.model.Agency;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.model.GrantingSystem;
import ca.gc.tri_agency.granting_data.model.SystemFundingCycle;
import ca.gc.tri_agency.granting_data.model.SystemFundingOpportunity;
import ca.gc.tri_agency.granting_data.model.file.FundingCycleDatasetRow;
import ca.gc.tri_agency.granting_data.model.file.ProgramFromFile;
import ca.gc.tri_agency.granting_data.repo.AgencyRepository;
import ca.gc.tri_agency.granting_data.repo.FundingOpportunityRepository;
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
	FundingOpportunityRepository foRepo;

	@Autowired
	AgencyRepository agencyRepo;

	@Autowired
	SystemFundingCycleRepository systemFundingCycleRepo;

	@Autowired
	GrantingSystemRepository grantingSystemRepo;

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
//		ClassLoader classLoader = getClass().getClassLoader();
//		File file = new File(classLoader.getResource(datasetAnalysisFolder).getFile());
//		for (File l : file.listFiles()) {
//			list.add(l);
//		}
		return list;
	}

	@Override
	public List<FundingCycleDatasetRow> getFundingCyclesFromFile(String filename) {
		Collection<FundingCycleDatasetRow> rows = null;

		Xcelite xcelite;
//		ClassLoader classLoader = getClass().getClassLoader();
//		File file = new File(classLoader.getResource(datasetAnalysisFolder + filename).getFile());
		xcelite = new Xcelite(new File(datasetAnalysisFolder + filename));
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
					map.put(targetFo.getExtId(), targetFo);

				}
				SystemFundingCycle newCycle = new SystemFundingCycle();
//				try {
//					newCycle.setCompYear(new SimpleDateFormat("yyyy").parse(row.getCompetition_Year()));
//				} catch (ParseException e) {
//					LOG.log(Level.WARN, "Invalid year:" + row.getCompetition_Year());
//				}
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

	@Override
	public int importProgramsFromFile() {
		int retval = 0;
		HashMap<String, Agency> agencyMap = new HashMap<String, Agency>();
		List<Agency> list = agencyRepo.findAll();
		agencyMap = new HashMap<String, Agency>();
		for (Agency a : list) {
			agencyMap.put(a.getAcronymEn(), a);
		}

		Collection<ProgramFromFile> programsFromFile = extractProgramsFromFile("programFile.xlsm");
		for (ProgramFromFile p : programsFromFile) {
			FundingOpportunity newFo = new FundingOpportunity();
			if (p.getNameEn().trim().length() == 0) {
				LOG.log(Level.WARN, "empty name, assuming empty row.  skipping row");
				continue;
			}
			newFo.setNameEn(p.getNameEn());
			newFo.setNameFr(p.getNameFr());
			newFo.setDivision(p.getDivision());
			newFo.setFundingType(p.getFundingType());
			Agency leadAgency = agencyMap.get(p.getLeadAgency());
			newFo.setLeadAgency(leadAgency);
			if (leadAgency == null) { // ASSUMPTION
				newFo.setLeadAgency(agencyMap.get("NSERC"));
			}

			// ASSUMPTION: IF BLANK, THEN ITS SINGLE. ??
			if (p.getNumberOfAgencies() == null) {
				p.setNumberOfAgencies("Single");
			}
			String numAgencyText = p.getNumberOfAgencies().toLowerCase();
			if (numAgencyText.indexOf("single", 0) >= 0) {
				newFo.getParticipatingAgencies().add(leadAgency);
			} else {
				if (numAgencyText.indexOf("tri") >= 0) {
					newFo.getParticipatingAgencies().addAll(agencyMap.values());
				} else {
					if (numAgencyText.indexOf("bi") >= 0) {
						newFo.getParticipatingAgencies().add(agencyMap.get("NSERC"));
						newFo.getParticipatingAgencies().add(agencyMap.get("SSHRC"));
					} else {
						LOG.log(Level.ERROR, "unknown config for 'number of agencies':: " + numAgencyText);
					}
				}
			}

			newFo.setFrequency(p.getFrequency());
			newFo.setApplyMethod(p.getApplyMethod());
			newFo.setAwardManagementSystem(p.getAwardManagementSystem());
			newFo.setProgramLeadName(p.getProgramLeadName());

			LOG.log(Level.INFO, "about to store:" + newFo);
			foRepo.save(newFo);
			retval++;
		}
		return retval;
	}

	public Collection<ProgramFromFile> extractProgramsFromFile(String filename) {
		Collection<ProgramFromFile> programs = null;

		Xcelite xcelite;
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(filename).getFile());
		xcelite = new Xcelite(file);
		// xcelite.
		XceliteSheet sheet = xcelite.getSheet(0);
		SheetReader<ProgramFromFile> reader = sheet.getBeanReader(ProgramFromFile.class);
		programs = reader.read();

		/*
		 * URI newFile = null; xcelite = new Xcelite();
		 * 
		 * File file = new File() final ClassLoader loader =
		 * Thread.currentThread().getContextClassLoader(); InputStream is =
		 * loader.getResourceAsStream(filename); final InputStreamReader isr = new
		 * InputStreamReader(is, StandardCharsets.UTF_8); xcelite = new Xcelite(is);
		 * final InputStreamReader isr = new InputStreamReader(is,
		 * StandardCharsets.UTF_8); final BufferedReader br = new BufferedReader(isr)) {
		 * return br.lines().map(l -> path + "/" + l).map(r ->
		 * loader.getResource(r)).collect(toList()); }
		 */

		return programs;
	}

	@Override
	public int linkSystemFO(long systemFoId, long foId) {
		SystemFundingOpportunity systemFo = systemFoRepo.getOne(systemFoId);
		FundingOpportunity fo = foRepo.getOne(foId);
		systemFo.setLinkedFundingOpportunity(fo);
		systemFoRepo.save(systemFo);
		return 1;
	}

}

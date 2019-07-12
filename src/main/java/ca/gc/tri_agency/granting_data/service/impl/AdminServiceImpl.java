package ca.gc.tri_agency.granting_data.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;

import ca.gc.tri_agency.granting_data.model.SystemFundingCycle;
import ca.gc.tri_agency.granting_data.model.file.FundingCycleDatasetRow;
import ca.gc.tri_agency.granting_data.repo.SystemFundingCycleRepository;
import ca.gc.tri_agency.granting_data.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	SystemFundingCycleRepository systemFundingCycleRepo;

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
		Collection<FundingCycleDatasetRow> fileRows = loadObjectList(filename);
		List<SystemFundingCycle> dbFundingCycles = systemFundingCycleRepo.findAll();
		for (FundingCycleDatasetRow row : fileRows) {
			for (SystemFundingCycle cycle : dbFundingCycles) {
				if (cycle.getExtId().compareTo(row.getFoCycle()) == 0) {
					fileRows.remove(row);
				}
			}
		}
		return new ArrayList<FundingCycleDatasetRow>(fileRows);
	}

	private Collection<FundingCycleDatasetRow> loadObjectList(String fileName) {
		Collection<FundingCycleDatasetRow> rows = null;

		Xcelite xcelite;
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("datasets/" + fileName).getFile());
		xcelite = new Xcelite(file);
		XceliteSheet sheet = xcelite.getSheet(0);
		SheetReader<FundingCycleDatasetRow> reader = sheet.getBeanReader(FundingCycleDatasetRow.class);
		rows = reader.read();

		return rows;

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

}

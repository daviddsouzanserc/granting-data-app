package ca.gc.tri_agency.granting_data.service;

import java.io.File;
import java.util.List;

import ca.gc.tri_agency.granting_data.model.file.FundingCycleDatasetRow;

public interface AdminService {
	public List<File> getDatasetFiles();

	public List<FundingCycleDatasetRow> getFundingCyclesFromFile(String filename);

	public List<String> generateActionableFoCycleIds(List<FundingCycleDatasetRow> foCycles);

}
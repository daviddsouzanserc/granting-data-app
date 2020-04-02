package ca.gc.tri_agency.granting_data.service;

import java.io.File;
import java.util.List;

import ca.gc.tri_agency.granting_data.model.GrantingSystem;
import ca.gc.tri_agency.granting_data.model.SystemFundingCycle;
import ca.gc.tri_agency.granting_data.model.SystemFundingOpportunity;
import ca.gc.tri_agency.granting_data.model.file.FundingCycleDatasetRow;

public interface AdminService {
	public List<File> getDatasetFiles();

	public List<FundingCycleDatasetRow> getFundingCyclesFromFile(String filename);

	public List<String> generateActionableFoCycleIds(List<FundingCycleDatasetRow> foCycles);

	public int applyChangesFromFileByIds(String filename, String[] idsToAction);

	public int linkSystemFO(long systemFoId, long foId);

	public int unlinkSystemFO(long systemFoId, long foId);

	SystemFundingOpportunity registerSystemFundingOpportunity(FundingCycleDatasetRow row, GrantingSystem targetSystem);

	SystemFundingCycle registerSystemFundingCycle(FundingCycleDatasetRow row, SystemFundingOpportunity targetSfo);

}
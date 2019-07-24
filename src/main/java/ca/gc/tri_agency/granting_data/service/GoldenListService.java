package ca.gc.tri_agency.granting_data.service;

import java.util.List;
import java.util.Map;

import ca.gc.tri_agency.granting_data.model.FundingCycle;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;

public interface GoldenListService {

	public List<FundingOpportunity> getGoldenList();

	public List<FundingOpportunity> getFoByNameEn(String nameEn);

	public Map<Long, FundingCycle> getFundingCycleByFundingOpportunityMap();
}

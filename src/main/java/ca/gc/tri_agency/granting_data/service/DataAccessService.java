package ca.gc.tri_agency.granting_data.service;

import java.util.List;

import ca.gc.tri_agency.granting_data.model.Agency;
import ca.gc.tri_agency.granting_data.model.FundingCycle;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.model.SystemFundingOpportunity;

public interface DataAccessService {
	List<SystemFundingOpportunity> getAllSystemFOs();

	SystemFundingOpportunity getSystemFO(long id);

	List<FundingOpportunity> getAllFundingOpportunities();

	FundingOpportunity getFundingOpportunity(long id);

	List<Agency> getAllAgencies();

	List<FundingCycle> getFundingCyclesByFoId(Long id);

}

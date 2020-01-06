package ca.gc.tri_agency.granting_data.service;

import javax.validation.Valid;

import ca.gc.tri_agency.granting_data.model.FundingCycle;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.model.GrantingCapability;
import ca.gc.tri_agency.granting_data.model.User;

public interface RestrictedDataService {
	FundingOpportunity saveFundingOpportunity(FundingOpportunity targetUpdate);

	void setFoLeadContributor(long foId, String leadUserDn);

	void setFoLeadContributor(long foId, User user);

	FundingCycle createOrUpdateFundingCycle(FundingCycle command);

	GrantingCapability createGrantingCapability(@Valid GrantingCapability command);

	FundingCycle updateFc(FundingCycle command, FundingCycle target);

}

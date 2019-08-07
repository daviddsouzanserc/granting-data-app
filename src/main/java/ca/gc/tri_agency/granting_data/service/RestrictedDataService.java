package ca.gc.tri_agency.granting_data.service;

import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.model.User;

public interface RestrictedDataService {
	FundingOpportunity saveFundingOpportunity(FundingOpportunity targetUpdate);

	void setFoLeadContributor(long foId, String leadUserDn);

	void setFoLeadContributor(long foId, User user);

}

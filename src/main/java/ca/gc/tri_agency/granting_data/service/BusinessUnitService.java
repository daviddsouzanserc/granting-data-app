package ca.gc.tri_agency.granting_data.service;

import java.util.List;

import ca.gc.tri_agency.granting_data.model.Agency;
import ca.gc.tri_agency.granting_data.model.BusinessUnit;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;

public interface BusinessUnitService {

	BusinessUnit findBusinessUnitById(Long id);
	
	List<BusinessUnit> findAllBusinessUnits();
	
	List<FundingOpportunity> getAllLocalizedFundingOpportunitiesByBusinessUnit(BusinessUnit bu);
	
	List<BusinessUnit> getAllLocalizedBusinessUnitsByAgency(Agency agency);

	BusinessUnit createBusinessUnit(BusinessUnit bu);

}

package ca.gc.tri_agency.granting_data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import ca.gc.tri_agency.granting_data.model.Agency;
import ca.gc.tri_agency.granting_data.model.BusinessUnit;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.repo.BusinessUnitRepository;
import ca.gc.tri_agency.granting_data.repo.FundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.security.annotations.AdminOnly;
import ca.gc.tri_agency.granting_data.service.BusinessUnitService;

@Service
public class BusinessUnitServiceImpl implements BusinessUnitService {
	
	@Autowired
	private FundingOpportunityRepository foRepo;
	
	private BusinessUnitRepository buRepo;
	
	@Autowired
	public BusinessUnitServiceImpl(BusinessUnitRepository buRepo) {
		this.buRepo = buRepo;
	}

	@Override
	public BusinessUnit findBusinessUnitById(Long id) {
		return buRepo.findById(id).
				orElseThrow(() -> new DataRetrievalFailureException("That Business Unit does not exist"));
	}
	
	@Override
	public List<BusinessUnit> findAllBusinessUnits() {
		return buRepo.findAll();
	}

	@Override
	public List<FundingOpportunity> findAllFundingOpportunitiesByBusinessUnit(BusinessUnit bu) {
		return foRepo.findByBusinessUnitOrderByNameFrAsc(bu);
	}

	@Override
	public List<BusinessUnit> findAllBusinessUnitsByAgency(Agency agency) {
		return buRepo.findByAgencyOrderByNameFrAsc(agency);
	}

	@Override
	@AdminOnly
	public BusinessUnit saveBusinessUnit(BusinessUnit bu) {
		return buRepo.save(bu);
	}

}

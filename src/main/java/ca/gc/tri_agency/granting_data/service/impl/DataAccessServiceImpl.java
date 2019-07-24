package ca.gc.tri_agency.granting_data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.model.SystemFundingOpportunity;
import ca.gc.tri_agency.granting_data.repo.FundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.service.DataAccessService;

@Service
public class DataAccessServiceImpl implements DataAccessService {

	@Autowired
	SystemFundingOpportunityRepository systemFoRepo;
	@Autowired
	FundingOpportunityRepository foRepo;

	@Override
	public List<SystemFundingOpportunity> getAllSystemFOs() {
		return systemFoRepo.findAll();
	}

	@Override
	public SystemFundingOpportunity getSystemFO(long id) {
		return systemFoRepo.getOne(id);
	}

	@Override
	public List<FundingOpportunity> getAllFundingOpportunities() {
		return foRepo.findAll();
	}

}

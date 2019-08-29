package ca.gc.tri_agency.granting_data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.gc.tri_agency.granting_data.model.Agency;
import ca.gc.tri_agency.granting_data.model.FundingCycle;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.model.GrantingCapability;
import ca.gc.tri_agency.granting_data.model.SystemFundingCycle;
import ca.gc.tri_agency.granting_data.model.SystemFundingOpportunity;
import ca.gc.tri_agency.granting_data.repo.AgencyRepository;
import ca.gc.tri_agency.granting_data.repo.FundingCycleRepository;
import ca.gc.tri_agency.granting_data.repo.FundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.repo.GrantingCapabilityRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingCycleRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.service.DataAccessService;

@Service
public class DataAccessServiceImpl implements DataAccessService {

	@Autowired
	SystemFundingOpportunityRepository systemFoRepo;
	@Autowired
	SystemFundingCycleRepository systemFundingCycleRepo;
	@Autowired
	FundingOpportunityRepository foRepo;
	@Autowired
	AgencyRepository agencyRepo;
	@Autowired
	FundingCycleRepository fundingCycleRepo;
	@Autowired
	GrantingCapabilityRepository grantingCapabilityRepo;

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

	@Override
	public FundingOpportunity getFundingOpportunity(long id) {
		return foRepo.getOne(id);
	}

	@Override
	public List<Agency> getAllAgencies() {
		return agencyRepo.findAll();
	}

	@Override
	public List<FundingCycle> getFundingCyclesByFoId(Long id) {
		return fundingCycleRepo.findByFundingOpportunityId(id);
	}

	@Override
	public List<SystemFundingCycle> getSystemFundingCyclesByFoId(Long id) {
		Long systemFoId;
		try {
			systemFoId = systemFoRepo.findByLinkedFundingOpportunityId(id).getId();

		} catch (RuntimeException e) {
			return null;
		}
		return systemFundingCycleRepo.findBySystemFundingOpportunityId(systemFoId);
	}

	@Override
	public List<GrantingCapability> getGrantingCapabilitiesByFoId(long id) {
		return grantingCapabilityRepo.findByFundingOpportunityId(id);

	}

}

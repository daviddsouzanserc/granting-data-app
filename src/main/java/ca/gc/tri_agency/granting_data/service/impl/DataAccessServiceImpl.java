package ca.gc.tri_agency.granting_data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.gc.tri_agency.granting_data.model.Agency;
import ca.gc.tri_agency.granting_data.model.FundingCycle;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.model.GrantingCapability;
import ca.gc.tri_agency.granting_data.model.SystemFundingCycle;
import ca.gc.tri_agency.granting_data.model.SystemFundingOpportunity;
import ca.gc.tri_agency.granting_data.model.util.FundingCycleInfo;
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
	@Autowired
	FundingCycleRepository fcRepo;

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
	public Map<String, FundingCycleInfo> getFundingCycleDataMapByYear(Long id) {
		Map<String, FundingCycleInfo> retval = new TreeMap<String, FundingCycleInfo>();
		List<FundingCycle> fcList = fundingCycleRepo.findByFundingOpportunityId(id);
		List<SystemFundingCycle> sfcList = getSystemFundingCyclesByFoId(id);
		for (FundingCycle fc : fcList) {
			FundingCycleInfo newItem = new FundingCycleInfo();
			String year = fc.getCompYear().toString().substring(0, 4);
			newItem.setYear(year);
			newItem.setFc(fc);
			retval.put(year, newItem);
		}
		for (SystemFundingCycle sfc : sfcList) {
			String year = sfc.getCompYear().toString().substring(0, 4);
			if (retval.containsKey(year)) {
				retval.get(year).setSfc(sfc);
			} else {
				FundingCycleInfo newItem = new FundingCycleInfo();
				newItem.setYear(year);
				newItem.setSfc(sfc);
				retval.put(year, newItem);
			}
		}
		return retval;
	}

	@Override
	public List<SystemFundingCycle> getSystemFundingCyclesByFoId(Long id) {
		SystemFundingOpportunity sysFo = systemFoRepo.findByLinkedFundingOpportunityId(id);
		if (sysFo == null) {
			return new ArrayList<SystemFundingCycle>();
		}
		return systemFundingCycleRepo.findBySystemFundingOpportunityId(sysFo.getId());
	}

	@Override
	public List<GrantingCapability> getGrantingCapabilitiesByFoId(long id) {
		return grantingCapabilityRepo.findByFundingOpportunityId(id);

	}

	@Override
	public List<FundingOpportunity> getFoByNameEn(String nameEn) {
		return foRepo.findByNameEn(nameEn);
	}

	@Override
	public Map<Long, FundingCycle> getFundingCycleByFundingOpportunityMap() {
		Map<Long, FundingCycle> retval = new HashMap<Long, FundingCycle>();
		List<FundingCycle> fundingCycles = fcRepo.findAll();
		for (FundingCycle fc : fundingCycles) {
			retval.put(fc.getFundingOpportunity().getId(), fc);
		}
		return retval;
	}

}

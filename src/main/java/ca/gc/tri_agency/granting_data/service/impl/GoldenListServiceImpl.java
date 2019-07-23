package ca.gc.tri_agency.granting_data.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.gc.tri_agency.granting_data.model.FundingCycle;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.repo.FundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.service.GoldenListService;

@Service
public class GoldenListServiceImpl implements GoldenListService {

	@Autowired
	FundingOpportunityRepository foRepo;

	@Override
	public List<FundingOpportunity> getGoldenList() {

//		List<FundingOpportunity> dbFundingOpps = foRepo.findAll();
//		List<String> retval = new ArrayList<String>();
//		for (FundingOpportunity fo : dbFundingOpps) {
//			retval.add(fo.getNameEn());
//		}
//
//		return retval;
		return foRepo.findAll();
	}

	@Override
	public List<FundingOpportunity> getFoByNameEn(String nameEn) {
		return foRepo.findByNameEn(nameEn);
	}

	@Override
	public Map<Long, FundingCycle> getFundingCycleByFundingOpportunityMap() {
		// TODO Auto-generated method stub
		return null;
	}

}

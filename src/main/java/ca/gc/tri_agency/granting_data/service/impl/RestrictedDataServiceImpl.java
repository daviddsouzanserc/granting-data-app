package ca.gc.tri_agency.granting_data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.model.User;
import ca.gc.tri_agency.granting_data.repo.AgencyRepository;
import ca.gc.tri_agency.granting_data.repo.FundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.service.RestrictedDataService;
import ca.gc.tri_agency.granting_data.service.UserRepo;

@Service
public class RestrictedDataServiceImpl implements RestrictedDataService {

	@Autowired
	SystemFundingOpportunityRepository systemFoRepo;
	@Autowired
	FundingOpportunityRepository foRepo;
	@Autowired
	AgencyRepository agencyRepo;
	@Autowired
	UserRepo userRepo;

	@Override
	public FundingOpportunity saveFundingOpportunity(FundingOpportunity targetUpdate) {
		return foRepo.save(targetUpdate);
	}

	@Override
	public void setFoLeadContributor(long foId, String leadUserDn) {
		if (leadUserDn == null) {
			return;
		}
		FundingOpportunity foToUpdate = foRepo.getOne(foId);
		User person = userRepo.findPerson(leadUserDn);
		foToUpdate.setProgramLeadName(person.getUsername());
		foToUpdate.setProgramLeadDn(leadUserDn);
		foRepo.save(foToUpdate);
	}

}

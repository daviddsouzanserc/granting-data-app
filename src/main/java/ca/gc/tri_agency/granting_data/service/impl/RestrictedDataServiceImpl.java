package ca.gc.tri_agency.granting_data.service.impl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import ca.gc.tri_agency.granting_data.model.FundingCycle;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.model.GrantingCapability;
import ca.gc.tri_agency.granting_data.model.User;
import ca.gc.tri_agency.granting_data.repo.AgencyRepository;
import ca.gc.tri_agency.granting_data.repo.FundingCycleRepository;
import ca.gc.tri_agency.granting_data.repo.FundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.repo.GrantingCapabilityRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.repoLdap.UserRepo;
import ca.gc.tri_agency.granting_data.security.SecurityUtils;
import ca.gc.tri_agency.granting_data.service.RestrictedDataService;

@Service
public class RestrictedDataServiceImpl implements RestrictedDataService {

	@Autowired
	SystemFundingOpportunityRepository systemFoRepo;
	@Autowired
	FundingOpportunityRepository foRepo;
	@Autowired
	FundingCycleRepository fcRepo;
	@Autowired
	AgencyRepository agencyRepo;
	@Autowired
	UserRepo userRepo;
	@Autowired
	GrantingCapabilityRepository grantingCapabilityRepo;

	@Override
	@PreAuthorize("hasRole('ROLE_MDM ADMIN')")
	public FundingOpportunity saveFundingOpportunity(FundingOpportunity targetUpdate) {
		return foRepo.save(targetUpdate);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_MDM ADMIN')")
	public void setFoLeadContributor(long foId, String leadUserDn) {
		if (leadUserDn == null) {
			// return;??
		}
		FundingOpportunity foToUpdate = foRepo.getOne(foId);
		User person = userRepo.findPerson(leadUserDn);
		foToUpdate.setProgramLeadName(person.getUsername());
		foToUpdate.setProgramLeadDn(leadUserDn);
		foRepo.save(foToUpdate);
	}

	@Override
	public void setFoLeadContributor(long foId, User user) {
		FundingOpportunity foToUpdate = foRepo.getOne(foId);
		foToUpdate.setProgramLeadDn(user.getDn());
		foToUpdate.setProgramLeadName(user.getUsername());
		foRepo.save(foToUpdate);
	}

	@Override
	public FundingCycle createOrUpdateFundingCycle(FundingCycle command) {
		FundingCycle retval = null;
		if (SecurityUtils.hasRole("MDM ADMIN")
				|| command.getFundingOpportunity().getProgramLeadDn().compareTo(SecurityUtils.getLdapUserDn()) == 0) {
			retval = fcRepo.save(command);
		} else {
			throw new AccessDeniedException("un-authorized to add cycles to funding opportunity");
		}
		return retval;
	}

	@Override
	public FundingCycle updateFc(FundingCycle command, FundingCycle target) {
		target.setFiscalYear(command.getFiscalYear());
		target.setEndDate(command.getEndDate());
		target.setExpectedApplications(command.getExpectedApplications());
		target.setIsOpen(command.getIsOpen());
		target.setStartDate(command.getStartDate());
		return fcRepo.save(target);

	}

	@Override
	@PreAuthorize("hasRole('ROLE_MDM ADMIN')")
	public GrantingCapability createGrantingCapability(@Valid GrantingCapability command) {

		return grantingCapabilityRepo.save(command);
	}

}

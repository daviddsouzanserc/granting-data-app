package ca.gc.tri_agency.granting_data.service.impl;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.stereotype.Service;

import ca.gc.tri_agency.granting_data.model.FundingCycle;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.model.User;
import ca.gc.tri_agency.granting_data.repo.AgencyRepository;
import ca.gc.tri_agency.granting_data.repo.FundingCycleRepository;
import ca.gc.tri_agency.granting_data.repo.FundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.repoLdap.UserRepo;
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

	private boolean checkCredentials(Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return false;
		}
		if (!auth.isAuthenticated() || auth.getPrincipal() instanceof String) {
			return false;
		}
		// Object principal = auth.getPrincipal();
		LdapUserDetails principal = (LdapUserDetails) auth.getPrincipal();
		Collection<? extends GrantedAuthority> userAuthorities = principal.getAuthorities();
		for (GrantedAuthority g : userAuthorities) {
			if (g.getAuthority().equals("ROLE_ADMIN")) { // checks if current logged in user is an admin
				return true;
			}
		}

		// checks if dn is the same as the selected funding opportunity
		Optional<FundingOpportunity> fo = foRepo.findById(id);
		String currentUser = principal.getDn();
		if (currentUser.equals(fo.get().getProgramLeadDn())) {
			return true;
		}

		return false;

	}

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

	@Override
	public void setFoLeadContributor(long foId, User user) {
		FundingOpportunity foToUpdate = foRepo.getOne(foId);
		foToUpdate.setProgramLeadDn(user.getDn());
		foToUpdate.setProgramLeadName(user.getUsername());
		foRepo.save(foToUpdate);
	}

	@Override
	public FundingCycle createOrUpdateFundingCycle(FundingCycle command) {
		// todo:: verify ownership, throw exception if user is not authorized
		if (checkCredentials(command.getFundingOpportunity().getId())) {
			return fcRepo.save(command);
		}
		return null; // should throw exception here
	}

}

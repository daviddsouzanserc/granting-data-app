package ca.gc.tri_agency.granting_data.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ca.gc.tri_agency.granting_data.model.Agency;
import ca.gc.tri_agency.granting_data.model.ApprovedApplication;
import ca.gc.tri_agency.granting_data.model.ApprovedApplicationParticipation;
import ca.gc.tri_agency.granting_data.model.DatasetApplicationRegistration;
import ca.gc.tri_agency.granting_data.model.Organization;
import ca.gc.tri_agency.granting_data.model.Program;
import ca.gc.tri_agency.granting_data.model.view.OrganizationWithLinkNum;
import ca.gc.tri_agency.granting_data.repo.AgencyRepository;
import ca.gc.tri_agency.granting_data.repo.DatasetAppRegistrationRepository;
import ca.gc.tri_agency.granting_data.repo.OrganizationRepository;
import ca.gc.tri_agency.granting_data.repo.ProgramRepository;
import ca.gc.tri_agency.granting_data.repo.ViewApprovedAppRegistrations;
import ca.gc.tri_agency.granting_data.repo.ViewApprovedApplications;
import ca.gc.tri_agency.granting_data.repo.view.ViewOrgsWithLinkNumRepository;
import ca.gc.tri_agency.granting_data.service.DataAccessService;

@Service
public class DataAccessServiceImpl implements DataAccessService {
	/** Logger */
	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	ProgramRepository programRepo;
	@Autowired
	AgencyRepository agencyRepo;
	@Autowired
	OrganizationRepository orgRepo;
	@Autowired
	ViewOrgsWithLinkNumRepository viewOrgsWithLinkNumRepo;
	@Autowired
	DatasetAppRegistrationRepository participationsRepo;

	@Autowired
	ViewApprovedAppRegistrations approvedAppParticipationsRepo;

	@Autowired
	ViewApprovedApplications approvedAppsRepo;

	public boolean isAdmin() {
		for (GrantedAuthority role : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			if (role.getAuthority().compareTo("ROLE_ADMIN") == 0) {
				return true;
			}
		}
		return false;
	}

	public String getUserAgencyAcronym() {
		String agencyAcronym = null;
		for (GrantedAuthority role : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			if (role.getAuthority().compareTo("ROLE_SSHRC") == 0) {
				agencyAcronym = "SSHRC";

			} else if (role.getAuthority().compareTo("ROLE_NSERC") == 0) {
				agencyAcronym = "NSERC";
			}
		}
		return agencyAcronym;

	}

	@Override
	public List<Program> getAllPrograms() {
		String agencyAcronym = null;
		List<Program> retval = null;
		if (isAdmin()) {
			retval = programRepo.findAll();
		} else {
			agencyAcronym = getUserAgencyAcronym();
			retval = programRepo.findByLeadAgencyAcronymEn(agencyAcronym);
		}
		return retval;
	}

	@Override
	public Program getProgram(Long id) {
		return programRepo.findById(id).orElse(null);
	}

	@Override
	public List<Agency> getAllAgencies() {
		return agencyRepo.findAll();
	}

	@Override
	public Program saveProgram(Program p) {
		return programRepo.save(p);
	}

	@Override
	public Agency getAgency(long id) {
		return agencyRepo.findById(id).orElse(null);
	}

	@Override
	public List<Program> getAgencyPrograms(long id) {
		return programRepo.findByLeadAgencyId(id);
	}

	@Override
	public List<Organization> getAllOrganizations() {
		return orgRepo.findAll();
	}

	@Override
	public Organization saveOrganization(Organization newOrg) {
		return orgRepo.save(newOrg);
	}

	@Override
	public Organization getOrganization(Long orgId) {
		return orgRepo.getOne(orgId);
	}

	@Override
	public List<OrganizationWithLinkNum> getAllOrganizationsWithLinkNum() {
		return viewOrgsWithLinkNumRepo.findAll();
	}

	@Override
	public List<ApprovedApplication> getApprovedApplications() {
		if (isAdmin()) {
		return approvedAppsRepo.findAll();
		} else {
			String agencyAcronym = getUserAgencyAcronym();
			return approvedAppsRepo.findByAgencyNameEn(agencyAcronym);
			
		}
	}

	@Override
	public ApprovedApplication getDatasetApplication(long id) {
		return approvedAppsRepo.getOne(id);
	}

	@Override
	public List<DatasetApplicationRegistration> getAppDatasetParticipations(long id) {
		return participationsRepo.findByDatasetApplicationId(id);
	}

	@Override
	public List<ApprovedApplicationParticipation> getApprovedParticipations() {
		if (isAdmin()) {
			return approvedAppParticipationsRepo.findAll();
		} else {
			String agencyAcronym = getUserAgencyAcronym();
			return approvedAppParticipationsRepo.findByAgencyNameEn(agencyAcronym);
		}
	}

	@Override
	public DatasetApplicationRegistration getAppDatasetParticipation(long id) {
		return participationsRepo.getOne(id);
	}

}

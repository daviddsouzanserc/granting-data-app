package ca.gc.tri_agency.granting_data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ca.gc.tri_agency.granting_data.model.ApplicationRegistrationsPerOrganization;
import ca.gc.tri_agency.granting_data.model.ApprovedApplicationParticipation;
import ca.gc.tri_agency.granting_data.model.ApprovedAward;
import ca.gc.tri_agency.granting_data.model.Dataset;
import ca.gc.tri_agency.granting_data.model.Dataset.DatasetStatus;
import ca.gc.tri_agency.granting_data.repo.DatasetRepository;
import ca.gc.tri_agency.granting_data.repo.ViewAppRegistrationPerOrganizationRepository;
import ca.gc.tri_agency.granting_data.repo.ViewApprovedAppRegistrations;
import ca.gc.tri_agency.granting_data.repo.ViewApprovedAwards;
import ca.gc.tri_agency.granting_data.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {
	@Autowired
	DatasetRepository datasetRepo;

	@Autowired
	ViewAppRegistrationPerOrganizationRepository appsPerOrgRepo;
	@Autowired
	ViewApprovedAppRegistrations viewApprovedAppRegistrations;
	@Autowired
	ViewApprovedAwards viewApprovedAwards;

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
	public List<ApplicationRegistrationsPerOrganization> getApplicationsPerOrganization() {
		return appsPerOrgRepo.findAll();
	}

	@Override
	public List<Dataset> getApprovedDatasets() {
		return datasetRepo.findByDatasetStatus(DatasetStatus.APPROVED);
	}

	@Override
	public List<ApprovedAward> getApprovedAwards() {
		if (isAdmin()) {
			return viewApprovedAwards.findAll();
		} else {
			return viewApprovedAwards.findByAgencyNameEn(getUserAgencyAcronym());
		}
	}

	@Override
	public List<ApprovedApplicationParticipation> getApprovedAppParticipations() {
		if (isAdmin()) {
			return viewApprovedAppRegistrations.findAll();
		} else {
			return viewApprovedAppRegistrations.findByAgencyNameEn(getUserAgencyAcronym());
		}
	}

}

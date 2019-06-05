package ca.gc.tri_agency.granting_data.service;

import java.util.List;

import ca.gc.tri_agency.granting_data.model.ApplicationRegistrationsPerOrganization;
import ca.gc.tri_agency.granting_data.model.ApprovedApplicationParticipation;
import ca.gc.tri_agency.granting_data.model.ApprovedAward;
import ca.gc.tri_agency.granting_data.model.Dataset;

public interface ReportService {

	// List<String> getYearsInput();

	// List<ApplicationRegistrationsPerOrganization>
	// getApplicationsPerOrganization(String year);
	List<ApplicationRegistrationsPerOrganization> getApplicationsPerOrganization();

	List<Dataset> getApprovedDatasets();

	List<ApprovedAward> getApprovedAwards();

	List<ApprovedApplicationParticipation> getApprovedAppParticipations();
}

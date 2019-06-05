package ca.gc.tri_agency.granting_data.service;

import java.util.List;

import ca.gc.tri_agency.granting_data.model.Agency;
import ca.gc.tri_agency.granting_data.model.ApprovedApplication;
import ca.gc.tri_agency.granting_data.model.ApprovedApplicationParticipation;
import ca.gc.tri_agency.granting_data.model.DatasetApplicationRegistration;
import ca.gc.tri_agency.granting_data.model.Organization;
import ca.gc.tri_agency.granting_data.model.Program;
import ca.gc.tri_agency.granting_data.model.view.OrganizationWithLinkNum;

public interface DataAccessService {

	public List<Program> getAllPrograms();

	public List<Agency> getAllAgencies();

	public Program saveProgram(Program p);

	public Program getProgram(Long id);

	public Agency getAgency(long id);

	public List<Program> getAgencyPrograms(long id);

	public List<Organization> getAllOrganizations();

	public Organization saveOrganization(Organization newOrg);

	public Organization getOrganization(Long orgId);

	public List<OrganizationWithLinkNum> getAllOrganizationsWithLinkNum();

	public List<ApprovedApplication> getApprovedApplications();

	public ApprovedApplication getDatasetApplication(long id);

	public List<DatasetApplicationRegistration> getAppDatasetParticipations(long id);

	public List<ApprovedApplicationParticipation> getApprovedParticipations();

	public DatasetApplicationRegistration getAppDatasetParticipation(long id);
	

}

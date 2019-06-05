package ca.gc.tri_agency.granting_data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DatasetApplicationRegistration {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "dataset_application_id")
	private DatasetApplication datasetApplication;

	@ManyToOne
	@JoinColumn(name = "dataset_person_id")
	private DatasetPerson person;

	@ManyToOne
	@JoinColumn(name = "registration_role_id")
	private DatasetAppRegistrationRole registrationRole;

	@ManyToOne
	@JoinColumn(name = "dataset_organization_id")
	private DatasetOrganization datasetOrganization;

	@ManyToOne
	@JoinColumn(name = "participation_edi_data_id")
	private ParticipationEdiData participationEdiData;

	public Long getId() {
		return id;
	}

	public DatasetPerson getPerson() {
		return person;
	}

	public void setPerson(DatasetPerson person) {
		this.person = person;
	}

	public DatasetOrganization getDatasetOrganization() {
		return datasetOrganization;
	}

	public void setDatasetOrganization(DatasetOrganization datasetOrganization) {
		this.datasetOrganization = datasetOrganization;
	}

	public DatasetAppRegistrationRole getRegistrationRole() {
		return registrationRole;
	}

	public void setRegistrationRole(DatasetAppRegistrationRole registrationRole) {
		this.registrationRole = registrationRole;
	}

	public DatasetApplication getDatasetApplication() {
		return datasetApplication;
	}

	public void setDatasetApplication(DatasetApplication datasetApplication) {
		this.datasetApplication = datasetApplication;
	}

	public ParticipationEdiData getParticipationEdiData() {
		return participationEdiData;
	}

	public void setParticipationEdiData(ParticipationEdiData participationEdiData) {
		this.participationEdiData = participationEdiData;
	}

}

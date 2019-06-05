package ca.gc.tri_agency.granting_data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
@Table(name = "report_application_registrations_per_organization", schema = "data_store")
public class ApplicationRegistrationsPerOrganization implements LocalizedParametersModel {
	@Id
	private Long id;

	private String nameEn;

	private String nameFr;

	@Column(name = "app_reg_num")
	private long appRegistrationCount;

	@Column(name = "app_num")
	private Long applicationCount;

	@Column(name = "program_num")
	private Long programCount;

	public ApplicationRegistrationsPerOrganization() {

	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameFr() {
		return nameFr;
	}

	public void setNameFr(String nameFr) {
		this.nameFr = nameFr;
	}

	public Long getId() {
		return id;
	}

	public long getAppRegistrationCount() {
		return appRegistrationCount;
	}

	public void setAppRegistrationCount(long appRegistrationCount) {
		this.appRegistrationCount = appRegistrationCount;
	}

	public Long getApplicationCount() {
		return applicationCount;
	}

	public void setApplicationCount(Long applicationCount) {
		this.applicationCount = applicationCount;
	}

	public Long getProgramCount() {
		return programCount;
	}

	public void setProgramCount(Long programCount) {
		this.programCount = programCount;
	}

}

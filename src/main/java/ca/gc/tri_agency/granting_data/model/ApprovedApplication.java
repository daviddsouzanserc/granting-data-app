package ca.gc.tri_agency.granting_data.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
@Table(name = "report_approved_applications", schema = "data_cabin")
public class ApprovedApplication implements LocalizedParametersModel {
	@Id
	private Long id;
	private Long applId;
	private Long programId;
	private String programYear;

	private String programNameEn;
	private String programNameFr;
	private Long agencyId;
	private String agencyNameEn;
	private String agencyNameFr;

	public ApprovedApplication() {

	}

	public Long getApplId() {
		return applId;
	}

	public void setApplId(Long applId) {
		this.applId = applId;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public String getProgramNameEn() {
		return programNameEn;
	}

	public void setProgramNameEn(String programNameEn) {
		this.programNameEn = programNameEn;
	}

	public String getProgramNameFr() {
		return programNameFr;
	}

	public void setProgramNameFr(String programNameFr) {
		this.programNameFr = programNameFr;
	}

	public Long getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Long agencyId) {
		this.agencyId = agencyId;
	}

	public String getAgencyNameEn() {
		return agencyNameEn;
	}

	public void setAgencyNameEn(String agencyNameEn) {
		this.agencyNameEn = agencyNameEn;
	}

	public String getAgencyNameFr() {
		return agencyNameFr;
	}

	public void setAgencyNameFr(String agencyNameFr) {
		this.agencyNameFr = agencyNameFr;
	}

	public Long getId() {
		return id;
	}

	public String getProgramYear() {
		return programYear;
	}

	public void setProgramYear(String programYear) {
		this.programYear = programYear;
	}
}

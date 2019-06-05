package ca.gc.tri_agency.granting_data.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
@Table(name = "report_approved_application_participations_edi", schema = "data_store")
public class ApprovedApplicationParticipationEdi implements LocalizedParametersModel {
	@Id
	private Long id;
	private Long applId;
	private Long programId;

	private String programNameEn;
	private String programNameFr;
	private Long agencyId;
	private String agencyNameEn;
	private String agencyNameFr;
	private Long orgId;
	private String orgNameEn;
	private String orgNameFr;
	private String orgCity;
	private String postalZipCode;

	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;
	private boolean dateOfBirthPreferNot;
	private String genderSelection;
	private String disabilityStatus;
	private String indIdentityResponse;
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean indIdRespPreferNot;
	private String disabilityResponse;
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean disabilityRespPreferNot;
	private String visibleMinorityResponse;
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean visMinRespPreferNot;

	public ApprovedApplicationParticipationEdi() {

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

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgNameEn() {
		return orgNameEn;
	}

	public void setOrgNameEn(String orgNameEn) {
		this.orgNameEn = orgNameEn;
	}

	public String getOrgNameFr() {
		return orgNameFr;
	}

	public void setOrgNameFr(String orgNameFr) {
		this.orgNameFr = orgNameFr;
	}

	public String getOrgCity() {
		return orgCity;
	}

	public void setOrgCity(String orgCity) {
		this.orgCity = orgCity;
	}

	public String getPostalZipCode() {
		return postalZipCode;
	}

	public void setPostalZipCode(String postalZipCode) {
		this.postalZipCode = postalZipCode;
	}

	public Long getId() {
		return id;
	}
}

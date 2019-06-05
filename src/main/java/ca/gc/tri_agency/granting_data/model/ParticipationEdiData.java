package ca.gc.tri_agency.granting_data.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
public class ParticipationEdiData implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

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

	// private String

	public ParticipationEdiData() {

	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public boolean isDateOfBirthPreferNot() {
		return dateOfBirthPreferNot;
	}

	public void setDateOfBirthPreferNot(boolean dateOfBirthPreferNot) {
		this.dateOfBirthPreferNot = dateOfBirthPreferNot;
	}

	public String getGenderSelection() {
		return genderSelection;
	}

	public void setGenderSelection(String genderSelection) {
		this.genderSelection = genderSelection;
	}

	public String getDisabilityStatus() {
		return disabilityStatus;
	}

	public void setDisabilityStatus(String disabilityStatus) {
		this.disabilityStatus = disabilityStatus;
	}

	public String getIndIdentityResponse() {
		return indIdentityResponse;
	}

	public void setIndIdentityResponse(String indIdentityResponse) {
		this.indIdentityResponse = indIdentityResponse;
	}

	public boolean isIndIdRespPreferNot() {
		return indIdRespPreferNot;
	}

	public void setIndIdRespPreferNot(boolean indIdRespPreferNot) {
		this.indIdRespPreferNot = indIdRespPreferNot;
	}

	public String getDisabilityResponse() {
		return disabilityResponse;
	}

	public void setDisabilityResponse(String disabilityResponse) {
		this.disabilityResponse = disabilityResponse;
	}

	public boolean isDisabilityRespPreferNot() {
		return disabilityRespPreferNot;
	}

	public void setDisabilityRespPreferNot(boolean disabilityRespPreferNot) {
		this.disabilityRespPreferNot = disabilityRespPreferNot;
	}

	public String getVisibleMinorityResponse() {
		return visibleMinorityResponse;
	}

	public void setVisibleMinorityResponse(String visibleMinorityResponse) {
		this.visibleMinorityResponse = visibleMinorityResponse;
	}

	public boolean isVisMinRespPreferNot() {
		return visMinRespPreferNot;
	}

	public void setVisMinRespPreferNot(boolean visMinRespPreferNot) {
		this.visMinRespPreferNot = visMinRespPreferNot;
	}

	public Long getId() {
		return id;
	}

}

package ca.gc.tri_agency.granting_data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
public class SystemAction implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String userDn;

	private Long referenceId;

	private SystemActionType actionType;

	private String message;

	public SystemAction() {

	}

	public enum SystemActionType {
		/** Some other format. */
		AUTHORIZATION_CHANGE, FUNDING_CYCLE_CHANGE, FUNDING_OPPORTUNITY_CHANGE, DATASET_UPLOAD
	}

	public String getUserDn() {
		return userDn;
	}

	public void setUserDn(String userDn) {
		this.userDn = userDn;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public SystemActionType getActionType() {
		return actionType;
	}

	public void setActionType(SystemActionType actionType) {
		this.actionType = actionType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getId() {
		return id;
	}

}

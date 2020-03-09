package ca.gc.tri_agency.granting_data.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
public class SystemFundingCycle implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String extId;

	private Long numAppsReceived;

	@Temporal(TemporalType.DATE)
	private Date fiscalYear;

	@ManyToOne
	@JoinColumn(name = "system_funding_opportunity_id")
	private SystemFundingOpportunity systemFundingOpportunity;

	/*
	 * could add: private SimpleDateFormat applyDeadlineDate;
	 */

	public SystemFundingCycle() {
		super();
	}

	public Long getId() {
		return id;
	}

	public String getExtId() {
		return extId;
	}

	public void setExtId(String extId) {
		this.extId = extId;
	}

	public Date getFiscalYear() {
		return fiscalYear;
	}

	public void setFiscalYear(Date compYear) {
		this.fiscalYear = compYear;
	}

	public SystemFundingOpportunity getSystemFundingOpportunity() {
		return systemFundingOpportunity;
	}

	public void setSystemFundingOpportunity(SystemFundingOpportunity systemFundingOpportunity) {
		this.systemFundingOpportunity = systemFundingOpportunity;
	}

	public Long getNumAppsReceived() {
		return numAppsReceived;
	}

	public void setNumAppsReceived(Long numAppsReceived) {
		this.numAppsReceived = numAppsReceived;
	}
}

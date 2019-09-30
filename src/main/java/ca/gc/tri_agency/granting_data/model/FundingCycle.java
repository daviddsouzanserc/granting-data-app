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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
public class FundingCycle implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy")
	private Date compYear;

	private boolean isOpen;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;

	@Min(1)
	@NotNull
	private Long expectedApplications;

	@ManyToOne
	@JoinColumn(name = "funding_opportunity_id")
	private FundingOpportunity fundingOpportunity;

	/*
	 * could add: private SimpleDateFormat applyDeadlineDate;
	 */

	public Long getId() {
		return id;
	}

	public FundingOpportunity getFundingOpportunity() {
		return fundingOpportunity;
	}

	public void setFundingOpportunity(FundingOpportunity program) {
		this.fundingOpportunity = program;
	}

	public Date getCompYear() {
		return compYear;
	}

	public void setCompYear(Date compYear) {
		this.compYear = compYear;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getExpectedApplications() {
		return expectedApplications;
	}

	public void setExpectedApplications(Long expectedApplications) {
		this.expectedApplications = expectedApplications;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public boolean getIsOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public void setIsOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
}

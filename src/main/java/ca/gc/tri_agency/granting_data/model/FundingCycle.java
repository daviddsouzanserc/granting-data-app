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

	@ManyToOne
	@JoinColumn(name = "fiscal_year_id")
	private FiscalYear fiscalYear;

	public FiscalYear getFiscalYear() {
		return fiscalYear;
	}

	public void setFiscalYear(FiscalYear fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	private boolean isOpen;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDateNOI;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDateLOI;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDateFullApplication;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDateNOI;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDateLOI;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDateFullApplication;

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

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStartDateNOI() {
		return startDateNOI;
	}

	public void setStartDateNOI(Date startDateNOI) {
		this.startDateNOI = startDateNOI;
	}

	public Date getStartDateLOI() {
		return startDateLOI;
	}

	public void setStartDateLOI(Date startDateLOI) {
		this.startDateLOI = startDateLOI;
	}

	public Date getStartDateFullApplication() {
		return startDateFullApplication;
	}

	public void setStartDateFullApplication(Date startDateFullApplication) {
		this.startDateFullApplication = startDateFullApplication;
	}

	public Date getEndDateNOI() {
		return endDateNOI;
	}

	public void setEndDateNOI(Date endDateNOI) {
		this.endDateNOI = endDateNOI;
	}

	public Date getEndDateLOI() {
		return endDateLOI;
	}

	public void setEndDateLOI(Date endDateLOI) {
		this.endDateLOI = endDateLOI;
	}

	public Date getEndDateFullApplication() {
		return endDateFullApplication;
	}

	public void setEndDateFullApplication(Date endDateFullApplication) {
		this.endDateFullApplication = endDateFullApplication;
	}
}

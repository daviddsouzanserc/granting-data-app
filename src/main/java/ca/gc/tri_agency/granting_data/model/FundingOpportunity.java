package ca.gc.tri_agency.granting_data.model;

//import java.util.Date; 
//or import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.springframework.context.i18n.LocaleContextHolder;

import ca.gc.tri_agency.granting_data.form.ProgramForm;
import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

import java.text.SimpleDateFormat;

@Entity
public class FundingOpportunity implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nameEn;

	private String nameFr;

	@ManyToOne
	@JoinColumn(name = "lead_agency_id")
	private Agency leadAgency;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable
	private Set<Agency> participatingAgencies;

	private String division; // could be dropped

	private String fundingType; // could be dropped

	private String frequency;

	private String applyMethod;

	private String awardManagementSystem;

	private String programLeadName;
	
	/* could be added:
	private Long numberOfApplications;
	
	private boolean participantComplexity; single/multiple applications
	
	private String programOfficerName;
	
	private String programAssistantName;
	
	private SimpleDateFormat applicationDeadline;

	*/
	public FundingOpportunity() {
	}

	public void loadFromForm(ProgramForm f) {
		this.setApplyMethod(f.getApplyMethod());
		this.setAwardManagementSystem(f.getAwardManagementSystem());
		this.setDivision(f.getDivision());
		this.setFundingType(f.getFundingType());
		this.setFrequency(f.getFrequency());
		this.setLeadAgency(f.getLeadAgency());
		this.setNameEn(f.getNameEn());
		this.setNameFr(f.getNameFr());
		this.setProgramLeadName(f.getProgramLeadName());
	}

	public String getName() {
		String retval = "";
		if (LocaleContextHolder.getLocale().toString().contains("en")) {
			retval = getNameEn();
		} else {
			retval = getNameFr();
		}
		return retval;
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

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getApplyMethod() {
		return applyMethod;
	}

	public void setApplyMethod(String applyMethod) {
		this.applyMethod = applyMethod;
	}

	public String getAwardManagementSystem() {
		return awardManagementSystem;
	}

	public void setAwardManagementSystem(String awardManagementSystem) {
		this.awardManagementSystem = awardManagementSystem;
	}

	public String getProgramLeadName() {
		return programLeadName;
	}

	public void setProgramLeadName(String programLeadName) {
		this.programLeadName = programLeadName;
	}
	public Agency getLeadAgency() {
		return leadAgency;
	}

	public void setLeadAgency(Agency leadAgency) {
		this.leadAgency = leadAgency;
	}

	public Long getId() {
		return id;
	}

	public String toString() {
		return "" + id + " : " + nameEn + " :: " + nameFr + " :: " + leadAgency;

	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getFundingType() {
		return fundingType;
	}

	public void setFundingType(String fundingType) {
		this.fundingType = fundingType;
	}
	
	/*
	
	public Long getNumberOfApplications() {
		return numberOfApplications;
	}
	
	public void setNumberOfApplications(Long numberOfApplications) {
		this.numberOfApplications = numberOfApplications;
	}
	
	public boolean getParticipantComplexity() {
		return participantComplexity;
	}
	
	public void setParticipantComplexity(boolean participantComplexity) {
		this.participantComplexity = participantComplexity;
	}
	
	public SimpleDateFormat getApplicationDealine() {
		return applicationdDeadline;
	}
	
	public void setApplicationDeadline(SimpleDateFormat applicationDeadline) {
		this.applicationDeadline = applicationDeadline;
	}
	
	public String getProgramOfficer() {
		return progranOfficer;
	}
	
	public void setPogramOfficer(String programOfficer) {
		this.programOfficer = programOfficer;
	}
	
	public String getProgramAssistant() {
		return programAssistance;
	}
	
	public void setProgramAssistant(String programAssistant) {
		this.getProgramAssistant() = programAssistant;
	}
	
	
	*/
}

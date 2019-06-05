package ca.gc.tri_agency.granting_data.model.file;

import java.util.Set;

import javax.persistence.OneToMany;

import com.ebay.xcelite.annotations.Column;

import ca.gc.tri_agency.granting_data.model.Agency;

public class ProgramFromFile {

	@Column(name = "French Translation for Funding Opportunity")
	private String nameFr;

	@Column(name = "Funding  Opportunity")
	private String nameEn;

	@Column(name = "Number of Agencies Involved")
	private String numberOfAgencies;

	@Column(name = "Lead Agency")
	private String leadAgency;

	@Column(name = "Program Lead")
	private String programLeadName;

	@Column(name = "Funding Opportunity Type")
	private String fundingType;

	@Column(name = "Division")
	private String division;

	@Column(name = "Frequency")
	private String frequency;

	@Column(name = " How do applicants apply?")
	private String applyMethod;

	@Column(name = "Where is the FO awarded?")
	private String awardManagementSystem;

	@OneToMany
	private Set<Agency> agencies;

	public String getNameFr() {
		return nameFr;
	}

	public void setNameFr(String nameFr) {
		this.nameFr = nameFr;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNumberOfAgencies() {
		return numberOfAgencies;
	}

	public void setNumberOfAgencies(String numberOfAgencies) {
		this.numberOfAgencies = numberOfAgencies;
	}

	public String getLeadAgency() {
		return leadAgency;
	}

	public void setLeadAgency(String leadAgency) {
		this.leadAgency = leadAgency;
	}

	public String getProgramLeadName() {
		return programLeadName;
	}

	public void setProgramLeadName(String programLeadName) {
		this.programLeadName = programLeadName;
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

	public Set<Agency> getAgencies() {
		return agencies;
	}

	public void setAgencies(Set<Agency> agencies) {
		this.agencies = agencies;
	}

	public String getFundingType() {
		return fundingType;
	}

	public void setFundingType(String fundingType) {
		this.fundingType = fundingType;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

}

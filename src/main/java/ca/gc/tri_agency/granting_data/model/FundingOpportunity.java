package ca.gc.tri_agency.granting_data.model;

//import java.util.Date; 
//or import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
public class FundingOpportunity implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nameEn;

	private String nameFr;

	@ManyToOne(fetch = FetchType.EAGER)
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

	public boolean isNOI;

	private boolean isLOI;

	private boolean isFullApplication;

	private String programLeadName;

	private String programLeadDn;

	public FundingOpportunity() {
		setParticipatingAgencies(new HashSet<Agency>());
	}

	public void loadFromForm(FundingOpportunity f) {
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

	private String partnerOrg;

	private boolean isEdiRequired;

	private boolean isComplex;

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

	public Set<Agency> getParticipatingAgencies() {
		return participatingAgencies;
	}

	public void setParticipatingAgencies(Set<Agency> participatingAgencies) {
		this.participatingAgencies = participatingAgencies;
	}

	public String getProgramLeadDn() {
		return programLeadDn;
	}

	public void setProgramLeadDn(String programLeadDn) {
		this.programLeadDn = programLeadDn;
	}

	private boolean isJointInitiative;

	public boolean isJointInitiative() {
		return isJointInitiative;
	}

	public boolean getIsJointInitiative() {
		return isJointInitiative;
	}

	public void setJointInitiative(boolean isJointInitiative) {
		this.isJointInitiative = isJointInitiative;
	}

	public String getPartnerOrg() {
		return partnerOrg;
	}

	public void setPartnerOrg(String partnerOrg) {
		this.partnerOrg = partnerOrg;
	}

	public boolean getIsEdiRequired() {
		return isEdiRequired;
	}

	public void setEdiRequired(boolean isEdiRequired) {
		this.isEdiRequired = isEdiRequired;
	}

	public boolean getIsComplex() {
		return isComplex;
	}

	public void setComplex(boolean isComplex) {
		this.isComplex = isComplex;
	}

	public boolean getIsNOI() {
		return isNOI;
	}

	public void setIsNOI(boolean nOI) {
		isNOI = nOI;
	}

	public boolean getIsLOI() {
		return isLOI;
	}

	public void setIsLOI(boolean islOI) {
		isLOI = islOI;
	}

	public boolean getIsFullApplication() {
		return isFullApplication;
	}

	public void setIsFullApplication(boolean fullApplication) {
		isFullApplication = fullApplication;
	}

}

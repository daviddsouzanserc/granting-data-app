package ca.gc.tri_agency.granting_data.form;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.context.i18n.LocaleContextHolder;

import ca.gc.tri_agency.granting_data.model.Agency;
import ca.gc.tri_agency.granting_data.model.Program;

public class ProgramForm {

	private Long id;

	@NotNull
	@Size(min = 2)
	private String nameEn;

	@NotNull
	@Size(min = 2)
	private String nameFr;

	@NotNull
	@Size(min = 1)
	private String division;

	@NotNull
	@Size(min = 2)
	private String fundingType;

	@NotNull
	@Size(min = 2)
	private String frequency;

	@NotNull
	@Size(min = 2)
	private String applyMethod;

	@NotNull
	@Size(min = 2)
	private String awardManagementSystem;

	@NotNull
	@Size(min = 2)
	private String programLeadName;

	@NotNull
	private Set<Agency> agencies;

	@NotNull
	private Agency leadAgency;

	public ProgramForm(Program p) {
		id = p.getId();
		nameEn = p.getNameEn();
		nameFr = p.getNameFr();
		this.setFundingType(p.getFundingType());
		this.setDivision(p.getDivision());
		this.frequency = p.getFrequency();
		this.applyMethod = p.getApplyMethod();
		this.awardManagementSystem = p.getAwardManagementSystem();
		this.programLeadName = p.getProgramLeadName();
		this.agencies = p.getAgencies();
		this.leadAgency = p.getLeadAgency();

	}

	public ProgramForm() {
		agencies = new HashSet<Agency>();
	}

	public String getNameEn() {
		return nameEn;
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

	public Set<Agency> getAgencies() {
		return agencies;
	}

	public void setAgencies(Set<Agency> agencies) {
		this.agencies = agencies;
	}

	public Agency getLeadAgency() {
		return leadAgency;
	}

	public void setLeadAgency(Agency leadAgency) {
		this.leadAgency = leadAgency;
	}

	public String toString() {
		return "" + id + " : " + nameEn + " :: " + nameFr;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
}

package ca.gc.tri_agency.granting_data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

import java.text.SimpleDateFormat;
@Entity
public class FundingCycle implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String extId;

	private String nameEn;

	private String nameFr;
	
	@ManyToOne
	@JoinColumn(name = "program_id")
	private Program program;
	
	/*could add:
	private SimpleDateFormat applyDeadlineDate;
	*/ 

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

	public Long getId() {
		return id;
	}

	public String getExtId() {
		return extId;
	}

	public void setExtId(String extId) {
		this.extId = extId;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}
	
	/*
	public SimpleDateFormat getApplyDeadlineDate() {
		return applyDeadlineDate;
	}

	public void setApplyDeadlineDate(SimpleDateFormat applyDeadlineDate) {
		this.applyDeadlineDate = applyDeadlineDate;
	}
	*/
}

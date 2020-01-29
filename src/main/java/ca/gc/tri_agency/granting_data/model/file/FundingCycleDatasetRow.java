package ca.gc.tri_agency.granting_data.model.file;

import com.ebay.xcelite.annotations.Column;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

public class FundingCycleDatasetRow implements LocalizedParametersModel {

	@Column(name = "FO_Cycle")
	private String foCycle;

//@Column(name = "Competition_Year")
	private Long competitionYear;

	@Column(name = "Program_ID")
	private String Program_ID;

	@Column(name = "Program_English")
	private String programNameEn;

	@Column(name = "Program_French")
	private String programNameFr;

	@Column(name = "Num_Received_Apps")
	private long numReceivedApps;

	public String getFoCycle() {
		return foCycle;
	}

	public void setFoCycle(String foCycle) {
		this.foCycle = foCycle;
	}

	public String getProgram_ID() {
		return Program_ID;
	}

	public void setProgram_ID(String program_ID) {
		Program_ID = program_ID;
	}

	public long getNumReceivedApps() {
		return numReceivedApps;
	}

	public void setNumReceivedApps(long numReceivedApps) {
		this.numReceivedApps = numReceivedApps;
	}

	public String getProgramNameEn() {
		return programNameEn;
	}

	public void setProgramNameEn(String programNameEn) {
		this.programNameEn = programNameEn;
	}

	public String getProgramNameFr() {
		return programNameFr;
	}

	public void setProgramNameFr(String programNameFr) {
		this.programNameFr = programNameFr;
	}

	public Long getCompetitionYear() {
		return competitionYear;
	}

	public void setCompetitionYear(Long competitionYear) {
		this.competitionYear = competitionYear;
	}
}

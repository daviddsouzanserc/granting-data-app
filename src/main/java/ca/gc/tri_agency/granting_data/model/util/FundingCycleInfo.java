package ca.gc.tri_agency.granting_data.model.util;

import ca.gc.tri_agency.granting_data.model.FundingCycle;
import ca.gc.tri_agency.granting_data.model.SystemFundingCycle;

public class FundingCycleInfo {
	FundingCycle fc;
	SystemFundingCycle sfc;
	String year;

	public FundingCycle getFc() {
		return fc;
	}

	public void setFc(FundingCycle fc) {
		this.fc = fc;
	}

	public SystemFundingCycle getSfc() {
		return sfc;
	}

	public void setSfc(SystemFundingCycle sfc) {
		this.sfc = sfc;
	}

	public void setYear(String year) {
		this.year = year;

	}

	public String getYear() {
		return this.year;

	}

}

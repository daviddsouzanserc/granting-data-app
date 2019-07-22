package ca.gc.tri_agency.granting_data.service;

import java.util.List;

import ca.gc.tri_agency.granting_data.model.FundingOpportunity;

public interface GoldenListService {

	public List<FundingOpportunity> getGoldenList();

	public List<FundingOpportunity> getFoByNameEn(String nameEn);
}

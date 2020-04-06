package ca.gc.tri_agency.granting_data.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.gc.tri_agency.granting_data.model.BusinessUnit;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;

@Repository
public interface FundingOpportunityRepository extends JpaRepository<FundingOpportunity, Long> {

	List<FundingOpportunity> findAllByNameEn(String nameEn);

	List<FundingOpportunity> findAllByLeadAgencyId(long id);

	List<FundingOpportunity> findAllByBusinessUnit(BusinessUnit bu);
	
}

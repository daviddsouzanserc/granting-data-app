package ca.gc.tri_agency.granting_data.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.SystemFundingOpportunity;

public interface SystemFundingOpportunityRepository extends JpaRepository<SystemFundingOpportunity, Long> {
	SystemFundingOpportunity findByLinkedFundingOpportunityId(Long id);

	List<SystemFundingOpportunity> findByExtId(String id);

	List<SystemFundingOpportunity> findByNameEn(String name);
}

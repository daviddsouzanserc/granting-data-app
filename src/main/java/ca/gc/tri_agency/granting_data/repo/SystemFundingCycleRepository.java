package ca.gc.tri_agency.granting_data.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.SystemFundingCycle;

public interface SystemFundingCycleRepository extends JpaRepository<SystemFundingCycle, Long> {
	List<SystemFundingCycle> findBySystemFundingOpportunityId(Long id);

	List<SystemFundingCycle> findByExtIdAndSystemFundingOpportunityId(String sfoName, long newSfoId);

}
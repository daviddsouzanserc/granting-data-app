package ca.gc.tri_agency.granting_data.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.gc.tri_agency.granting_data.model.GrantingCapability;

@Repository
public interface GrantingCapabilityRepository extends JpaRepository<GrantingCapability, Long> {

	List<GrantingCapability> findByFundingOpportunityId(Long id); // TODO: refactor its implementation

	List<GrantingCapability> findByGrantingStageNameEn(String name);

}

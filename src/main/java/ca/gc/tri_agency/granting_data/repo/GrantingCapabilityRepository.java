package ca.gc.tri_agency.granting_data.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.GrantingCapability;

public interface GrantingCapabilityRepository extends JpaRepository<GrantingCapability, Long> {

	List<GrantingCapability> findByFundingOpportunityId(long id);
}

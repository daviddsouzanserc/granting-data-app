package ca.gc.tri_agency.granting_data.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.GrantingStage;

public interface GrantingStageRepository extends JpaRepository<GrantingStage, Long> {

}

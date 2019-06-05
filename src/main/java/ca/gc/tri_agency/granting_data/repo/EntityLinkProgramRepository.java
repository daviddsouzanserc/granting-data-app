package ca.gc.tri_agency.granting_data.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.EntityLinkProgram;

public interface EntityLinkProgramRepository extends JpaRepository<EntityLinkProgram, Long> {
	List<EntityLinkProgram> findByDatasetConfigurationId(Long long1);

}

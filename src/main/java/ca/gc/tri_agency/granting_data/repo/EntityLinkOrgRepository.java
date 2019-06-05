package ca.gc.tri_agency.granting_data.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.EntityLinkOrganization;

public interface EntityLinkOrgRepository extends JpaRepository<EntityLinkOrganization, Long> {
	List<EntityLinkOrganization> findByDatasetConfigurationId(Long long1);

}

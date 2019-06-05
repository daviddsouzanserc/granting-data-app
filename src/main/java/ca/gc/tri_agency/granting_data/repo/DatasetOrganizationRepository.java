package ca.gc.tri_agency.granting_data.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.DatasetOrganization;

public interface DatasetOrganizationRepository extends JpaRepository<DatasetOrganization, Long> {

	List<DatasetOrganization> findByDatasetIdAndEntityLinkIsNull(long configId);

	List<DatasetOrganization> findByDatasetId(long configId);

}

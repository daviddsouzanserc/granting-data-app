package ca.gc.tri_agency.granting_data.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.DatasetConfiguration;

public interface DatasetConfigurationRepository extends JpaRepository<DatasetConfiguration, Long> {

	DatasetConfiguration findOneByAcronym(String targetAcronym);

}

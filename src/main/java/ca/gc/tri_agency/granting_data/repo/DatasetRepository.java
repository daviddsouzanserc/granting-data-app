package ca.gc.tri_agency.granting_data.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.Dataset;
import ca.gc.tri_agency.granting_data.model.Dataset.DatasetStatus;
import ca.gc.tri_agency.granting_data.model.Dataset.DatasetType;

public interface DatasetRepository extends JpaRepository<Dataset, Long> {

	List<Dataset> findByDatasetStatus(DatasetStatus approved);

	List<Dataset> findByDatasetStatusAndDatasetType(DatasetStatus approved, DatasetType datasetType);

}

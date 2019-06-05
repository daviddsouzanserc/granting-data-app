package ca.gc.tri_agency.granting_data.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.DatasetProgram;

public interface DatasetProgramRepository extends JpaRepository<DatasetProgram, Long> {

	List<DatasetProgram> findByDatasetIdAndEntityLinkIsNull(long configId);

}

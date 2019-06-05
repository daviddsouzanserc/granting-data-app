package ca.gc.tri_agency.granting_data.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.DatasetApplicationRegistration;

public interface DatasetAppRegistrationRepository extends JpaRepository<DatasetApplicationRegistration, Long> {

	List<DatasetApplicationRegistration> findByDatasetApplicationId(long id);

}

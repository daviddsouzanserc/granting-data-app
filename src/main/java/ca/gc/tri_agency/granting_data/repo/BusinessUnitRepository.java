package ca.gc.tri_agency.granting_data.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.gc.tri_agency.granting_data.model.BusinessUnit;
import ca.gc.tri_agency.granting_data.model.Agency;
import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, Long> {

	List<BusinessUnit> findByAgencyOrderByNameEnAsc(Agency agency);
	
	List<BusinessUnit> findByAgencyOrderByNameFrAsc(Agency agency);
	
}

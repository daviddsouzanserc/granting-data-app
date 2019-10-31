package ca.gc.tri_agency.granting_data.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.FiscalYear;

public interface FiscalYearRepository extends JpaRepository<FiscalYear, Long> {

//	List<String> findAllByYear(Long year);

}

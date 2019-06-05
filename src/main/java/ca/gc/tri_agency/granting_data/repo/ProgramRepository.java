package ca.gc.tri_agency.granting_data.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.Program;

public interface ProgramRepository extends JpaRepository<Program, Long> {
	List<Program> findByLeadAgencyId(long id);

	List<Program> findByLeadAgencyAcronymEn(String agencyAcronym);

}

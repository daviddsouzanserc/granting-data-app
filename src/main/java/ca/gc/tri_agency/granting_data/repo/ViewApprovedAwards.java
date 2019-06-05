package ca.gc.tri_agency.granting_data.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.ApprovedAward;

public interface ViewApprovedAwards extends JpaRepository<ApprovedAward, Long> {

	List<ApprovedAward> findByAgencyNameEn(String userAgencyAcronym);

}

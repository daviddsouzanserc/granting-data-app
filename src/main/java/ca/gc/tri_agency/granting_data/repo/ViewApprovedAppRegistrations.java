package ca.gc.tri_agency.granting_data.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.ApprovedApplicationParticipation;

public interface ViewApprovedAppRegistrations extends JpaRepository<ApprovedApplicationParticipation, Long> {

	List<ApprovedApplicationParticipation> findByAgencyNameEn(String userAgencyAcronym);

}

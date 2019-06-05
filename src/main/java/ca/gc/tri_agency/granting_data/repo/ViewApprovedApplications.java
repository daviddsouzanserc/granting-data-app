package ca.gc.tri_agency.granting_data.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.ApprovedApplication;

public interface ViewApprovedApplications extends JpaRepository<ApprovedApplication, Long> {

	List<ApprovedApplication> findByAgencyNameEn(String userAgencyAcronym);

}

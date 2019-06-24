package ca.gc.tri_agency.granting_data.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.FundingOpportunity;

public interface ProgramRepository extends JpaRepository<FundingOpportunity,Long>{

}

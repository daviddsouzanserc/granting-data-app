package ca.gc.tri_agency.granting_data.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.FundingCycle;

public interface FundingCycleRepository extends JpaRepository<FundingCycle, Long> {

	List<FundingCycle> findByFundingOpportunityId(Long id);

	List<FundingCycle> findAllByStartDateGreaterThanEqualAndStartDateLessThanOrEndDateGreaterThanEqualAndEndDateLessThan(
			Date startDate, Date endDate, Date startDate2, Date endDate2);

}

package ca.gc.tri_agency.granting_data.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import ca.gc.tri_agency.granting_data.model.Agency;
import ca.gc.tri_agency.granting_data.model.FiscalYear;
import ca.gc.tri_agency.granting_data.model.FundingCycle;
import ca.gc.tri_agency.granting_data.model.FundingOpportunity;
import ca.gc.tri_agency.granting_data.model.GrantingCapability;
import ca.gc.tri_agency.granting_data.model.GrantingSystem;
import ca.gc.tri_agency.granting_data.model.SystemFundingCycle;
import ca.gc.tri_agency.granting_data.model.SystemFundingOpportunity;
import ca.gc.tri_agency.granting_data.model.util.FundingCycleInfo;
import ca.gc.tri_agency.granting_data.repo.AgencyRepository;
import ca.gc.tri_agency.granting_data.repo.FiscalYearRepository;
import ca.gc.tri_agency.granting_data.repo.FundingCycleRepository;
import ca.gc.tri_agency.granting_data.repo.FundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.repo.GrantingCapabilityRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingCycleRepository;
import ca.gc.tri_agency.granting_data.repo.SystemFundingOpportunityRepository;
import ca.gc.tri_agency.granting_data.security.annotations.AdminOnly;
import ca.gc.tri_agency.granting_data.service.DataAccessService;

@Service
public class DataAccessServiceImpl implements DataAccessService {

	@Autowired
	private SystemFundingOpportunityRepository systemFoRepo;
	@Autowired
	private SystemFundingCycleRepository systemFundingCycleRepo;
	@Autowired
	private FundingOpportunityRepository foRepo;
	@Autowired
	private AgencyRepository agencyRepo;
	@Autowired
	private FundingCycleRepository fundingCycleRepo;
	@Autowired
	private GrantingCapabilityRepository grantingCapabilityRepo;
	@Autowired
	private FundingCycleRepository fcRepo;
	@Autowired
	private FiscalYearRepository fyRepo;

	@Override
	public List<SystemFundingOpportunity> getAllSystemFOs() {
		return systemFoRepo.findAll();
	}

	@Override
	public SystemFundingOpportunity getSystemFO(long id) {
		return systemFoRepo.findById(id)
				.orElseThrow(() -> new DataRetrievalFailureException("That System Funding Opportunity does not exist"));
	}

	@Override
	public List<FundingOpportunity> getAllFundingOpportunities() {
		return foRepo.findAll();
	}

	@Override
	public FundingOpportunity getFundingOpportunity(long id) {
		return foRepo.findById(id)
				.orElseThrow(() -> new DataRetrievalFailureException("That Funding Opportunity does not exist"));
	}

	@Override
	public List<Agency> getAllAgencies() {
		return agencyRepo.findAll();
	}

	@Override
	public List<FundingCycle> getFundingCyclesByFoId(Long id) {
		return fundingCycleRepo.findByFundingOpportunityId(id);
	}

	@Override
	public Map<String, FundingCycleInfo> getFundingCycleDataMapByYear(Long id) {
		Map<String, FundingCycleInfo> retval = new TreeMap<String, FundingCycleInfo>();
		List<FundingCycle> fcList = fundingCycleRepo.findByFundingOpportunityId(id);
		List<SystemFundingCycle> sfcList = getSystemFundingCyclesByFoId(id);
		for (FundingCycle fc : fcList) {
			FundingCycleInfo newItem = new FundingCycleInfo();
			String year = fc.getFiscalYear().toString().substring(0, 4);
			newItem.setYear(year);
			newItem.setFc(fc);
			retval.put(year, newItem);
		}
		for (SystemFundingCycle sfc : sfcList) {
			String year = sfc.getFiscalYear().toString().substring(0, 4);
			if (retval.containsKey(year)) {
				retval.get(year).setSfc(sfc);
			} else {
				FundingCycleInfo newItem = new FundingCycleInfo();
				newItem.setYear(year);
				newItem.setSfc(sfc);
				retval.put(year, newItem);
			}
		}
		return retval;
	}

	@Override
	public List<SystemFundingCycle> getSystemFundingCyclesByFoId(Long id) {
		SystemFundingOpportunity sysFo = systemFoRepo.findByLinkedFundingOpportunityId(id);
		if (sysFo == null) {
			return new ArrayList<SystemFundingCycle>();
		}
		return systemFundingCycleRepo.findBySystemFundingOpportunityId(sysFo.getId());
	}

	@Override
	public List<GrantingCapability> getGrantingCapabilitiesByFoId(long id) {
		return grantingCapabilityRepo.findByFundingOpportunityId(id);

	}

	@Override
	public List<FundingOpportunity> getFoByNameEn(String nameEn) {
		return foRepo.findAllByNameEn(nameEn);
	}

	@Override
	public Map<Long, FundingCycle> getFundingCycleByFundingOpportunityMap() {
		Map<Long, FundingCycle> retval = new HashMap<Long, FundingCycle>();
		List<FundingCycle> fundingCycles = fcRepo.findAll();
		for (FundingCycle fc : fundingCycles) {
			retval.put(fc.getFundingOpportunity().getId(), fc);
		}
		return retval;
	}

	@Override
	public Agency getAgency(long id) {
		return agencyRepo.findById(id)
				.orElseThrow(() -> new DataRetrievalFailureException("That Agency does not exist"));
	}

	@Override
	public List<FundingOpportunity> getAgencyFundingOpportunities(long id) {
		return foRepo.findAllByLeadAgencyId(id);
	}

	@Override
	public Map<String, List<FundingCycle>> getMonthlyFundingCyclesMapByDate(long plusMinusMonth) {
		Map<String, List<FundingCycle>> retval = new HashMap<String, List<FundingCycle>>();
		LocalDate now = LocalDate.now();
		Date startDate, endDate;
		if (plusMinusMonth == 0) {
			startDate = java.sql.Date.valueOf(now);
			endDate = java.sql.Date.valueOf(now.plusMonths(1));
		} else if (plusMinusMonth < 0) {
			startDate = java.sql.Date.valueOf(now.minusMonths(plusMinusMonth * -1));
			endDate = java.sql.Date.valueOf(now.minusMonths((plusMinusMonth * -1) + 1));
		} else {
			startDate = java.sql.Date.valueOf(now.plusMonths(plusMinusMonth));
			endDate = java.sql.Date.valueOf(now.plusMonths(plusMinusMonth + 1));
		}

		// GET FCs THAT HAVE A START OR END DATE WITHIN THE RANGE (TARGET MONTH)
		List<FundingCycle> fcList = fcRepo
				.findAllByStartDateGreaterThanEqualAndStartDateLessThanOrEndDateGreaterThanEqualAndEndDateLessThan(
						startDate, endDate, startDate, endDate);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		for (FundingCycle fc : fcList) {
			String startDateKey = formatter.format(fc.getStartDate());
			String endDateKey = formatter.format(fc.getEndDate());
			if (retval.get(startDateKey) == null) {
				retval.put(startDateKey, new ArrayList<FundingCycle>());
			}
			if (retval.get(endDateKey) == null) {
				retval.put(endDateKey, new ArrayList<FundingCycle>());
			}
			retval.get(startDateKey).add(fc);
			retval.get(endDateKey).add(fc);
		}
		return retval;
	}

	@Override
	public FundingCycle getFundingCycle(long id) {
		return fcRepo.findById(id)
				.orElseThrow(() -> new DataRetrievalFailureException("That Funding Cycle does not exist"));
	}

	public List<FundingCycle> getAllFundingCycles() {
		return fcRepo.findAll();
	}

	@Override
	public List<FiscalYear> findAllFiscalYears() {
		// TODO Auto-generated method stub
		return fyRepo.findAll();
	}

	@SuppressWarnings("null")
	@Override
	public List<FiscalYear> fiscalYears() {
		List<FiscalYear> fy = fyRepo.findAll();
		return fy;
	}

	@SuppressWarnings("null")
	@Override
	public List<FundingCycle> fundingCyclesByFiscalYearId(Long Id) {
		List<FundingCycle> fc = fcRepo.findAll();
		List<FundingCycle> fcNew = new ArrayList<FundingCycle>();

		for (FundingCycle e : fc) {
			if (e.getFiscalYear().getId() == Id) {
				fcNew.add(e);
			}

		}

		return fcNew;
	}

	@Override
	public void createFy(Long year) {
		FiscalYear fy = new FiscalYear();
		fy.setYear(year);
		fyRepo.save(fy);

	}

	public String getEmail(String dn) {
		// String x = userRepo.g
		return null;

	}

	@AdminOnly
	@Override
	public void createFo(FundingOpportunity fo) {
		// TODO Auto-generated method stub
		foRepo.save(fo);

	}

	@Override
	public Map<String, List<FundingCycle>> getAllStartingDates(Long plusMinusMonth) {

		Map<String, List<FundingCycle>> retval = new HashMap<String, List<FundingCycle>>();
		LocalDate now = LocalDate.now();
		Date startDate, endDate;
		if (plusMinusMonth == 0) {
			startDate = java.sql.Date.valueOf(now);
			endDate = java.sql.Date.valueOf(now.plusMonths(1));
		} else if (plusMinusMonth < 0) {
			startDate = java.sql.Date.valueOf(now.minusMonths(plusMinusMonth * -1));
			endDate = java.sql.Date.valueOf(now.minusMonths((plusMinusMonth * -1) + 1));
		} else {
			startDate = java.sql.Date.valueOf(now.plusMonths(plusMinusMonth));
			endDate = java.sql.Date.valueOf(now.plusMonths(plusMinusMonth + 1));
		}

		// GET FCs THAT HAVE A START OR END DATE WITHIN THE RANGE (TARGET MONTH)
		List<FundingCycle> fcList = fcRepo
				.findAllByStartDateGreaterThanEqualAndStartDateLessThanOrEndDateGreaterThanEqualAndEndDateLessThan(
						startDate, endDate, startDate, endDate);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		for (FundingCycle fc : fcList) {
			String startDateKey = formatter.format(fc.getStartDate());
			if (retval.get(startDateKey) == null) {
				retval.put(startDateKey, new ArrayList<FundingCycle>());
			}
			retval.get(startDateKey).add(fc);

		}
		return retval;
	}

	@Override
	public Map<String, List<FundingCycle>> getAllEndingDates(Long plusMinusMonth) {

		Map<String, List<FundingCycle>> retval = new HashMap<String, List<FundingCycle>>();
		LocalDate now = LocalDate.now();
		Date startDate, endDate;
		if (plusMinusMonth == 0) {
			startDate = java.sql.Date.valueOf(now);
			endDate = java.sql.Date.valueOf(now.plusMonths(1));
		} else if (plusMinusMonth < 0) {
			startDate = java.sql.Date.valueOf(now.minusMonths(plusMinusMonth * -1));
			endDate = java.sql.Date.valueOf(now.minusMonths((plusMinusMonth * -1) + 1));
		} else {
			startDate = java.sql.Date.valueOf(now.plusMonths(plusMinusMonth));
			endDate = java.sql.Date.valueOf(now.plusMonths(plusMinusMonth + 1));
		}

		List<FundingCycle> fcList = fcRepo
				.findAllByStartDateGreaterThanEqualAndStartDateLessThanOrEndDateGreaterThanEqualAndEndDateLessThan(
						startDate, endDate, startDate, endDate);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		for (FundingCycle fc : fcList) {
			String endDateKey = formatter.format(fc.getEndDate());

			if (retval.get(endDateKey) == null) {
				retval.put(endDateKey, new ArrayList<FundingCycle>());
			}

			retval.get(endDateKey).add(fc);
		}
		return retval;
	}

	@Override
	public Map<String, List<FundingCycle>> getAllDatesNOIStart(long plusMinusMonth) {
		Map<String, List<FundingCycle>> retval = new HashMap<String, List<FundingCycle>>();
		LocalDate now = LocalDate.now();
		Date startDate, endDate;
		if (plusMinusMonth == 0) {
			startDate = java.sql.Date.valueOf(now);
			endDate = java.sql.Date.valueOf(now.plusMonths(1));
		} else if (plusMinusMonth < 0) {
			startDate = java.sql.Date.valueOf(now.minusMonths(plusMinusMonth * -1));
			endDate = java.sql.Date.valueOf(now.minusMonths((plusMinusMonth * -1) + 1));
		} else {
			startDate = java.sql.Date.valueOf(now.plusMonths(plusMinusMonth));
			endDate = java.sql.Date.valueOf(now.plusMonths(plusMinusMonth + 1));
		}

		// GET FCs THAT HAVE A START OR END DATE WITHIN THE RANGE (TARGET MONTH)
		List<FundingCycle> fcList = fcRepo
				.findAllByStartDateGreaterThanEqualAndStartDateLessThanOrEndDateGreaterThanEqualAndEndDateLessThan(
						startDate, endDate, startDate, endDate);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		for (FundingCycle fc : fcList) {
			String startDateKey = formatter.format(fc.getStartDateNOI());
			String endDateKey = formatter.format(fc.getEndDateNOI());
			if (retval.get(startDateKey) == null) {
				retval.put(startDateKey, new ArrayList<FundingCycle>());
			}
			if (retval.get(endDateKey) == null) {
				retval.put(endDateKey, new ArrayList<FundingCycle>());
			}
			retval.get(startDateKey).add(fc);
		}
		return retval;
	}

	@Override
	public Map<String, List<FundingCycle>> getAllDatesNOIEnd(long plusMinusMonth) {
		Map<String, List<FundingCycle>> retval = new HashMap<String, List<FundingCycle>>();
		LocalDate now = LocalDate.now();
		Date startDate, endDate;
		if (plusMinusMonth == 0) {
			startDate = java.sql.Date.valueOf(now);
			endDate = java.sql.Date.valueOf(now.plusMonths(1));
		} else if (plusMinusMonth < 0) {
			startDate = java.sql.Date.valueOf(now.minusMonths(plusMinusMonth * -1));
			endDate = java.sql.Date.valueOf(now.minusMonths((plusMinusMonth * -1) + 1));
		} else {
			startDate = java.sql.Date.valueOf(now.plusMonths(plusMinusMonth));
			endDate = java.sql.Date.valueOf(now.plusMonths(plusMinusMonth + 1));
		}

		// GET FCs THAT HAVE A START OR END DATE WITHIN THE RANGE (TARGET MONTH)
		List<FundingCycle> fcList = fcRepo
				.findAllByStartDateGreaterThanEqualAndStartDateLessThanOrEndDateGreaterThanEqualAndEndDateLessThan(
						startDate, endDate, startDate, endDate);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		for (FundingCycle fc : fcList) {
			String startDateKey = formatter.format(fc.getStartDateNOI());
			String endDateKey = formatter.format(fc.getEndDateNOI());
			if (retval.get(startDateKey) == null) {
				retval.put(startDateKey, new ArrayList<FundingCycle>());
			}
			if (retval.get(endDateKey) == null) {
				retval.put(endDateKey, new ArrayList<FundingCycle>());
			}
			retval.get(startDateKey).add(fc);
		}
		return retval;
	}

	@Override
	public Map<String, List<FundingCycle>> getAllDatesLOIStart(long plusMinusMonth) {
		Map<String, List<FundingCycle>> retval = new HashMap<String, List<FundingCycle>>();
		LocalDate now = LocalDate.now();
		Date startDate, endDate;
		if (plusMinusMonth == 0) {
			startDate = java.sql.Date.valueOf(now);
			endDate = java.sql.Date.valueOf(now.plusMonths(1));
		} else if (plusMinusMonth < 0) {
			startDate = java.sql.Date.valueOf(now.minusMonths(plusMinusMonth * -1));
			endDate = java.sql.Date.valueOf(now.minusMonths((plusMinusMonth * -1) + 1));
		} else {
			startDate = java.sql.Date.valueOf(now.plusMonths(plusMinusMonth));
			endDate = java.sql.Date.valueOf(now.plusMonths(plusMinusMonth + 1));
		}

		// GET FCs THAT HAVE A START OR END DATE WITHIN THE RANGE (TARGET MONTH)
		List<FundingCycle> fcList = fcRepo
				.findAllByStartDateGreaterThanEqualAndStartDateLessThanOrEndDateGreaterThanEqualAndEndDateLessThan(
						startDate, endDate, startDate, endDate);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		for (FundingCycle fc : fcList) {
			String startDateKey = formatter.format(fc.getStartDateLOI());
			String endDateKey = formatter.format(fc.getEndDateLOI());
			if (retval.get(startDateKey) == null) {
				retval.put(startDateKey, new ArrayList<FundingCycle>());
			}
			if (retval.get(endDateKey) == null) {
				retval.put(endDateKey, new ArrayList<FundingCycle>());
			}
			retval.get(startDateKey).add(fc);
		}
		return retval;
	}

	@Override
	public Map<String, List<FundingCycle>> getAllDatesLOIEnd(long plusMinusMonth) {
		Map<String, List<FundingCycle>> retval = new HashMap<String, List<FundingCycle>>();
		LocalDate now = LocalDate.now();
		Date startDate, endDate;
		if (plusMinusMonth == 0) {
			startDate = java.sql.Date.valueOf(now);
			endDate = java.sql.Date.valueOf(now.plusMonths(1));
		} else if (plusMinusMonth < 0) {
			startDate = java.sql.Date.valueOf(now.minusMonths(plusMinusMonth * -1));
			endDate = java.sql.Date.valueOf(now.minusMonths((plusMinusMonth * -1) + 1));
		} else {
			startDate = java.sql.Date.valueOf(now.plusMonths(plusMinusMonth));
			endDate = java.sql.Date.valueOf(now.plusMonths(plusMinusMonth + 1));
		}

		// GET FCs THAT HAVE A START OR END DATE WITHIN THE RANGE (TARGET MONTH)
		List<FundingCycle> fcList = fcRepo
				.findAllByStartDateGreaterThanEqualAndStartDateLessThanOrEndDateGreaterThanEqualAndEndDateLessThan(
						startDate, endDate, startDate, endDate);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		for (FundingCycle fc : fcList) {
			String startDateKey = formatter.format(fc.getStartDateLOI());
			String endDateKey = formatter.format(fc.getEndDateLOI());
			if (retval.get(startDateKey) == null) {
				retval.put(startDateKey, new ArrayList<FundingCycle>());
			}
			if (retval.get(endDateKey) == null) {
				retval.put(endDateKey, new ArrayList<FundingCycle>());
			}
			retval.get(endDateKey).add(fc);
		}
		return retval;
	}

	@Override
	public Map<Long, GrantingSystem> getApplySystemsByFundingOpportunityMap() {
		Map<Long, GrantingSystem> retval = new HashMap<Long, GrantingSystem>();
		List<GrantingCapability> applyCapabilities = grantingCapabilityRepo.findByGrantingStageNameEn("APPLY");
		for (GrantingCapability c : applyCapabilities) {
			retval.put(c.getFundingOpportunity().getId(), c.getGrantingSystem());
		}
		return retval;
	}

	@Override
	public Map<Long, List<GrantingSystem>> getAwardSystemsByFundingOpportunityMap() {
		Map<Long, List<GrantingSystem>> retval = new HashMap<Long, List<GrantingSystem>>();
		List<GrantingCapability> applyCapabilities = grantingCapabilityRepo.findByGrantingStageNameEn("AWARD");
		for (GrantingCapability c : applyCapabilities) {
			if (retval.containsKey(c.getFundingOpportunity().getId()) == false) {
				retval.put(c.getFundingOpportunity().getId(), new ArrayList<GrantingSystem>());
			}
			retval.get(c.getFundingOpportunity().getId()).add(c.getGrantingSystem());
		}
		return retval;
	}
}

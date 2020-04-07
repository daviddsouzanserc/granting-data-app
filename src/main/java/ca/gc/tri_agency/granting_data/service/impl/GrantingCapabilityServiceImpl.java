package ca.gc.tri_agency.granting_data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import ca.gc.tri_agency.granting_data.model.GrantingCapability;
import ca.gc.tri_agency.granting_data.repo.GrantingCapabilityRepository;
import ca.gc.tri_agency.granting_data.security.annotations.AdminOnly;
import ca.gc.tri_agency.granting_data.service.GrantingCapabilityService;

@Service
public class GrantingCapabilityServiceImpl implements GrantingCapabilityService {

	private GrantingCapabilityRepository gcRepo;

	@Autowired
	public GrantingCapabilityServiceImpl(GrantingCapabilityRepository gcRepo) {
		this.gcRepo = gcRepo;
	}

	@Override
	public GrantingCapability findGrantingCapabilityById(Long id) {
		return gcRepo.findById(id)
				.orElseThrow(() -> new DataRetrievalFailureException("That Granting Capability does not exist"));
	}

	@Override
	public List<GrantingCapability> findAllGrantingCapabilities() {
		return gcRepo.findAll();
	}

	@Override
	@AdminOnly
	public GrantingCapability saveGrantingCapability(GrantingCapability gc) {
		return gcRepo.save(gc);
	}

	@Override
	public List<GrantingCapability> findGrantingCapabilitiesByFoId(Long id) {
		return gcRepo.findByFundingOpportunityId(id);
	}

	@Override
	@AdminOnly
	public void deleteGrantingCapability(Long id) {
		gcRepo.delete(findGrantingCapabilityById(id));
	}
}

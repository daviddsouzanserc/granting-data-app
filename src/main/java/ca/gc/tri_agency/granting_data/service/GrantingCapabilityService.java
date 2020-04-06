package ca.gc.tri_agency.granting_data.service;

import java.util.List;

import ca.gc.tri_agency.granting_data.model.GrantingCapability;

public interface GrantingCapabilityService {

	GrantingCapability findGrantingCapabilityById(Long id);

	List<GrantingCapability> findAllGrantingCapabilities();

	GrantingCapability saveGrantingCapability(GrantingCapability gc);

	List<GrantingCapability> findGrantingCapabilitiesByFoId(Long id);
}

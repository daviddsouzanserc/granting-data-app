package ca.gc.tri_agency.granting_data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ca.gc.tri_agency.granting_data.service.GrantingCapabilityService;

@Controller
public class GrantingCapabilityController {

	private GrantingCapabilityService gcService;

	@Autowired
	public GrantingCapabilityController(GrantingCapabilityService gcService) {
		this.gcService = gcService;
	}
}

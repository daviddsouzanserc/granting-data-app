package ca.gc.tri_agency.granting_data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class GrantingCapability {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String description;

	private String url;

	@ManyToOne
	@JoinColumn(name = "funding_opportunity_id")
	private FundingOpportunity fundingOpportunity;

	@ManyToOne
	@JoinColumn(name = "core_function_id")
	private GrantingStage coreFunction;

	@ManyToOne
	@JoinColumn(name = "granting_system_id")
	private GrantingSystem grantingSystem;

	public Long getId() {
		return id;
	}

	public FundingOpportunity getFundingOpportunity() {
		return fundingOpportunity;
	}

	public void setFundingOpportunity(FundingOpportunity program) {
		this.fundingOpportunity = program;
	}

	public GrantingStage getCoreFunction() {
		return coreFunction;
	}

	public void setCoreFunction(GrantingStage coreFunction) {
		this.coreFunction = coreFunction;
	}

	public GrantingSystem getGrantingSystem() {
		return grantingSystem;
	}

	public void setGrantingSystem(GrantingSystem grantingSystem) {
		this.grantingSystem = grantingSystem;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}

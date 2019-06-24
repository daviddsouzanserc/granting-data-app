package ca.gc.tri_agency.granting_data.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class LeadContributor {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	private String email;
	
	@ManyToOne
	@JoinColumn(name = "funding_opportunity_id")
	private FundingOpportunity fundingOpportunity;

	@ManyToOne
	@JoinColumn(name = "aduser_id")
	private ADUser aduser;
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public FundingOpportunity getFundingOpportunity() {
		return fundingOpportunity;
	}

	public void setFundingOpportunity(FundingOpportunity program) {
		this.fundingOpportunity = program;
	}

	public ADUser getAduser() {
		return aduser;
	}

	public void setAduser(ADUser aduser) {
		this.aduser = aduser;
	}
}

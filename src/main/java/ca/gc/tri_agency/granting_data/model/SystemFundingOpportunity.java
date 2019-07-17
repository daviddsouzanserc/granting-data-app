package ca.gc.tri_agency.granting_data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
public class SystemFundingOpportunity implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String extId;

	private String nameEn;

	private String nameFr;

	@ManyToOne
	@JoinColumn(name = "linked_funding_opportunity_id")
	private FundingOpportunity linkedFundingOpportunity;

	@ManyToOne
	@JoinColumn(name = "granting_system_id")
	private GrantingSystem grantingSystem;

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameFr() {
		return nameFr;
	}

	public void setNameFr(String nameFr) {
		this.nameFr = nameFr;
	}

	public Long getId() {
		return id;
	}

	public String getExtId() {
		return extId;
	}

	public void setExtId(String extId) {
		this.extId = extId;
	}

	public GrantingSystem getGrantingSystem() {
		return grantingSystem;
	}

	public void setGrantingSystem(GrantingSystem grantingSystem) {
		this.grantingSystem = grantingSystem;
	}

	public FundingOpportunity getLinkedFundingOpportunity() {
		return linkedFundingOpportunity;
	}

	public void setLinkedFundingOpportunity(FundingOpportunity linkedFundingOpportunity) {
		this.linkedFundingOpportunity = linkedFundingOpportunity;
	}

}

package ca.gc.tri_agency.granting_data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
public class DatasetConfiguration implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	protected String nameEn;

	protected String nameFr;

	// @Column(unique = true)
	private String acronym;

	// private String

	@ManyToOne
	@JoinColumn(name = "default_agency_id")
	private Agency defaultAgencyForIncommingPrograms;

	@ManyToOne
	@JoinColumn(name = "grant_system_capability_id")
	private GrantSystemCapability grantSystemCapability;

	public DatasetConfiguration() {

	}

	public DatasetConfiguration(String nameEn, String nameFr, String acronym) {
		this.setNameEn(nameEn);
		this.setNameFr(nameFr);
		// this.setAcronym(acronym);
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public GrantSystemCapability getGrantSystemCapability() {
		return grantSystemCapability;
	}

	public void setGrantSystemCapability(GrantSystemCapability grantSystemCapability) {
		this.grantSystemCapability = grantSystemCapability;
	}

	// public String getName() {
	// String retval = "";
	// if (LocaleContextHolder.getLocale().toString().contains("en")) {
	// retval = getNameEn();
	// } else {
	// retval = getNameFr();
	// }
	// return retval;
	// }
	//
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

	public Agency getDefaultAgencyForIncommingPrograms() {
		return defaultAgencyForIncommingPrograms;
	}

	public void setDefaultAgencyForIncommingPrograms(Agency defaultAgencyForIncommingPrograms) {
		this.defaultAgencyForIncommingPrograms = defaultAgencyForIncommingPrograms;
	}

}

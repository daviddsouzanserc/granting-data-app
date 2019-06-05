package ca.gc.tri_agency.granting_data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
public class GrantSystemCapability implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	protected String nameEn;

	protected String nameFr;

	protected String acronymFr;

	protected String acronymEn;

	@ManyToOne
	@JoinColumn(name = "granting_system_id")
	private GrantingSystem grantingSystem;

	/** General type indicator. */
	@Column(name = "granting_function")
	@Enumerated(EnumType.STRING)
	private GrantingFunction grantingFunction;

	public enum GrantingFunction {
		/** Some other format. */
		ADMIN, APPLY, ASSESS, AWARD, AQUIT
	}

	public GrantSystemCapability() {

	}

	public GrantSystemCapability(String nameEn, String nameFr, String acronymEn, String acronymnFr) {
		this.setNameEn(nameEn);
		this.setNameFr(nameFr);
		this.setAcronymEn(acronymEn);
		this.setAcronymFr(acronymnFr);
	}

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

	public String getAcronymFr() {
		return acronymFr;
	}

	public void setAcronymFr(String acronymFr) {
		this.acronymFr = acronymFr;
	}

	public String getAcronymEn() {
		return acronymEn;
	}

	public void setAcronymEn(String acronymEn) {
		this.acronymEn = acronymEn;
	}

	public Long getId() {
		return id;
	}

	public GrantingSystem getGrantingSystem() {
		return grantingSystem;
	}

	public void setGrantingSystem(GrantingSystem grantingSystem) {
		this.grantingSystem = grantingSystem;
	}

	public GrantingFunction getGrantingFunction() {
		return grantingFunction;
	}

	public void setGrantingFunction(GrantingFunction grantingFunction) {
		this.grantingFunction = grantingFunction;
	}

}

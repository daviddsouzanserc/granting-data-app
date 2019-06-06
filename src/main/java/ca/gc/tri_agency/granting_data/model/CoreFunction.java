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
public class CoreFunction implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	protected String nameEn;

	protected String nameFr;

	protected String acronym;

	@Column(name = "granting_function")
	@Enumerated(EnumType.STRING)
	private GrantingFunction grantingFunction;

	public enum GrantingFunction {
		/** Some other format. */
		ADMIN, APPLY, ASSESS, AWARD, AQUIT
	}

	public CoreFunction() {

	}

	public CoreFunction(String nameEn, String nameFr, String acronym) {
		this.setNameEn(nameEn);
		this.setNameFr(nameFr);
		this.setAcronym(acronym);
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

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public Long getId() {
		return id;
	}


	public GrantingFunction getGrantingFunction() {
		return grantingFunction;
	}

	public void setGrantingFunction(GrantingFunction grantingFunction) {
		this.grantingFunction = grantingFunction;
	}

}

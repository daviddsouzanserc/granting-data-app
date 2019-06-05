package ca.gc.tri_agency.granting_data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
public class GrantingSystem implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	protected String nameEn;

	protected String nameFr;// awards management information system (AMIS)
	// Système inform
	// atisé de gestion des subventions et bourses (AMIS)
	protected String acronymFr;

	protected String acronymEn;

	public GrantingSystem() {

	}

	public GrantingSystem(String nameEn, String nameFr, String acronymEn, String acronymnFr) {
		this.setNameEn(nameEn);
		this.setNameFr(nameFr);
		this.setAcronymEn(acronymEn);
		this.setAcronymFr(acronymnFr);
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
	// public String getAcronym() {
	// String retval = "";
	// if (LocaleContextHolder.getLocale().toString().contains("en")) {
	// retval = getAcronymEn();
	// } else {
	// retval = getAcronymFr();
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

}

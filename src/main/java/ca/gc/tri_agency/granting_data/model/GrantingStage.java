package ca.gc.tri_agency.granting_data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.context.i18n.LocaleContextHolder;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
public class GrantingStage implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

//	@Column(name = "granting_function")
//	private String grantingFunction;

	protected String nameEn;

	protected String nameFr;

	public GrantingStage() {

	}

	public String getName() {
		String retval = "";
		if (LocaleContextHolder.getLocale().toString().contains("en")) {
			retval = getNameEn();
		} else {
			retval = getNameFr();
		}
		return retval;
	}

	public String getNameFr() {
		return nameFr;
	}

	public String getNameEn() {
		return nameEn;
	}

	public Long getId() {
		return id;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public void setNameFr(String nameFr) {
		this.nameFr = nameFr;
	}

}

package ca.gc.tri_agency.granting_data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.springframework.context.i18n.LocaleContextHolder;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
public class GrantingStage implements LocalizedParametersModel {
	@Id
	@SequenceGenerator(name = "SEQ_GRANTING_STAGE", sequenceName = "SEQ_GRANTING_STAGE", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GRANTING_STAGE")
	private Long id;

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

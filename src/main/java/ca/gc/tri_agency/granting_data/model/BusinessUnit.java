package ca.gc.tri_agency.granting_data.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.context.i18n.LocaleContextHolder;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
public class BusinessUnit implements LocalizedParametersModel {

	@Id
	@SequenceGenerator(name = "SEQ_BUSINESS_UNIT", sequenceName = "SEQ_BUSINESS_UNIT", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BUSINESS_UNIT")
	private Long id;
	
	@NotBlank
	private String nameEn;

	@NotBlank
	private String nameFr;

	@NotBlank
	private String acronymEn;

	@NotBlank
	private String acronymFr;

	@ManyToOne(optional = false)
	@JoinColumn(name = "agency_id")
	@NotNull
	private Agency agency;

	public BusinessUnit() {
	}
	
	public BusinessUnit(@NotBlank String nameEn, @NotBlank String nameFr,
			@NotBlank String acronymEn, @NotBlank String acronymFr, @NotNull Agency agency) {
		this.nameEn = nameEn;
		this.nameFr = nameFr;
		this.acronymEn = acronymEn;
		this.acronymFr = acronymFr;
		this.agency = agency;
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

	public String getAcronymEn() {
		return acronymEn;
	}

	public void setAcronymEn(String acronymEn) {
		this.acronymEn = acronymEn;
	}

	public String getAcronymFr() {
		return acronymFr;
	}

	public void setAcronymFr(String acronymFr) {
		this.acronymFr = acronymFr;
	}

	public Agency getAgency() {
		return agency;
	}

	public void setAgency(Agency agency) {
		this.agency = agency;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getAcronym() {
		return LocaleContextHolder.getLocale().getLanguage().equals("en") ? getAcronymEn() : getAcronymFr();
	}

	public String getName() {
		return LocaleContextHolder.getLocale().getLanguage().equals("en") ? getNameEn() : getNameFr();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BusinessUnit [id=");
		builder.append(id);
		builder.append(", nameEn=");
		builder.append(nameEn);
		builder.append(", nameFr=");
		builder.append(nameFr);
		builder.append(", acronymEn=");
		builder.append(acronymEn);
		builder.append(", acronymFr=");
		builder.append(acronymFr);
		builder.append(", agency=");
		builder.append(agency.getAcronym());
		builder.append("]");
		return builder.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusinessUnit other = (BusinessUnit) obj;
		return Objects.equals(acronymEn, other.acronymEn) && Objects.equals(acronymFr, other.acronymFr)
				&& Objects.equals(nameEn, other.nameEn) && Objects.equals(nameFr, other.nameFr);
	}

}

package ca.gc.tri_agency.granting_data.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
public class Organization implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nameEn;

	private String nameFr;

	@CreationTimestamp
	private LocalDateTime createDateTime;

	public Organization() {

	}

	public Organization(String nameEn, String nameFr) {
		this.setNameEn(nameEn);
		this.setNameFr(nameFr);
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

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

}

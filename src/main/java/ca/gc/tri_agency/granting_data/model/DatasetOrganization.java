package ca.gc.tri_agency.granting_data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
public class DatasetOrganization implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "entity_link_id", nullable = true)
	private EntityLinkOrganization entityLink;

	private String extId;

	private String nameEn;

	private String nameFr;
	
	private String city;
	
	private String postalZipCode;
	
	private String stateProvCode;

	@ManyToOne
	@JoinColumn(name = "dataset_id")
	private Dataset dataset;

	@Override
	public String toString() {
		return "" + id + " : " + extId + " : " + nameEn + " / " + nameFr;
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

	public String getExtId() {
		return extId;
	}

	public void setExtId(String extId) {
		this.extId = extId;
	}

	public EntityLinkOrganization getEntityLink() {
		return entityLink;
	}

	public void setEntityLink(EntityLinkOrganization entityLink) {
		this.entityLink = entityLink;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalZipCode() {
		return postalZipCode;
	}

	public void setPostalZipCode(String postalZipCode) {
		this.postalZipCode = postalZipCode;
	}

	public String getStateProvCode() {
		return stateProvCode;
	}

	public void setStateProvCode(String stateProvCode) {
		this.stateProvCode = stateProvCode;
	}
}

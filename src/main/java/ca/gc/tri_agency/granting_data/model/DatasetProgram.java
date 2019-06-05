package ca.gc.tri_agency.granting_data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
public class DatasetProgram implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String extId;

	@ManyToOne
	@JoinColumn(name = "agency_id")
	private Agency leadAgency;

	@ManyToOne
	@JoinColumn(name = "dataset_id")
	private Dataset dataset;

	private String nameEn;

	private String nameFr;

	@ManyToOne
	@JoinColumn(name = "entity_link_id", nullable = true)
	private EntityLinkProgram entityLink;

	public String toString() {
		return "DatasetProgram: " + id + " : " + extId + " : " + nameEn + " : " + nameFr;
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

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public EntityLinkProgram getEntityLink() {
		return entityLink;
	}

	public void setEntityLink(EntityLinkProgram entityLink) {
		this.entityLink = entityLink;
	}

	public Agency getLeadAgency() {
		return leadAgency;
	}

	public void setLeadAgency(Agency leadAgency) {
		this.leadAgency = leadAgency;
	}

}

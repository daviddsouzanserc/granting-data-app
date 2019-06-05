package ca.gc.tri_agency.granting_data.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
public class DatasetAward implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "dataset_application_id")
	private DatasetApplication datasetApplication;

	@ManyToOne
	@JoinColumn(name = "dataset_person_id")
	private DatasetPerson datasetPerson;

	private float amount;

	@Temporal(TemporalType.DATE)
	private Date programYear;

	//@Temporal(TemporalType.DATE)
	private Date fundingYear;

	public DatasetApplication getDatasetApplication() {
		return datasetApplication;
	}

	public void setDatasetApplication(DatasetApplication datasetApplication) {
		this.datasetApplication = datasetApplication;
	}

	public DatasetPerson getDatasetPerson() {
		return datasetPerson;
	}

	public void setDatasetPerson(DatasetPerson datasetPerson) {
		this.datasetPerson = datasetPerson;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public Date getProgramYear() {
		return programYear;
	}

	public void setProgramYear(Date programYear) {
		this.programYear = programYear;
	}

	public Long getId() {
		return id;
	}

	public Date getFundingYear() {
		return fundingYear;
	}

	public void setFundingYear(Date fundingYear) {
		this.fundingYear = fundingYear;
	}

}

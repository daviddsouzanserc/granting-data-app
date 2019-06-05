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

@Entity
public class DatasetApplication {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Long extId;

	private String extIdentifier;

	@ManyToOne
	@JoinColumn(name = "dataset_id")
	private Dataset dataset;

	@ManyToOne
	@JoinColumn(name = "dataset_program_id")
	private DatasetProgram datasetProgram;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDateTime;

	@Temporal(TemporalType.DATE)
	private Date programYear;

	public String toString() {
		return "Application: " + id + " : " + extId + " : " + extIdentifier;
	}

	public Date getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Long getId() {
		return id;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public DatasetProgram getDatasetProgram() {
		return datasetProgram;
	}

	public void setDatasetProgram(DatasetProgram datasetProgram) {
		this.datasetProgram = datasetProgram;
	}

	public Long getExtId() {
		return extId;
	}

	public void setExtId(Long extId) {
		this.extId = extId;
	}

	public String getExtIdentifier() {
		return extIdentifier;
	}

	public void setExtIdentifier(String extIdentifier) {
		this.extIdentifier = extIdentifier;
	}

	public Date getProgramYear() {
		return programYear;
	}

	public void setProgramYear(Date programYear) {
		this.programYear = programYear;
	}

}

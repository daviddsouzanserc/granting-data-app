package ca.gc.tri_agency.granting_data.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Dataset {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String filename;

	@ManyToOne
	@JoinColumn(name = "parent_dataset_id")
	private Dataset parentDataset;

	@ManyToOne
	@JoinColumn(name = "dataset_configuration_id")
	private DatasetConfiguration datasetConfiguration;

	@CreationTimestamp
	private LocalDateTime createDateTime;

	@UpdateTimestamp
	private LocalDateTime updateDateTime;

	private long totalRecords;

	private long currentRow;

	/** General type indicator. */
	@Column(name = "dataset_status")
	@Enumerated(EnumType.STRING)
	private DatasetStatus datasetStatus;

	public enum DatasetStatus {
		/** Some other format. */
		CREATED, UPLOADING, UPLOADING_ERROR, UPLOAD_COMPLETE, NORMALIZING, NORMALIZING_ERROR, NORMALIZED, ASSESS, APPROVED, TO_DELETE, ERROR
	}

	@Column(name = "dataset_type")
	@Enumerated(EnumType.STRING)
	private DatasetType datasetType;

	public enum DatasetType {
		/** Some other format. */
		APPLICATIONS, AWARDS, PAYMENTS
	}

	public DatasetStatus getDatasetStatus() {
		return datasetStatus;
	}

	public void setDatasetStatus(DatasetStatus datasetStatus) {
		this.datasetStatus = datasetStatus;
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	public LocalDateTime getUpdateDateTime() {
		return updateDateTime;
	}

	public void setUpdateDateTime(LocalDateTime updateDateTime) {
		this.updateDateTime = updateDateTime;
	}

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public long getCurrentRow() {
		return currentRow;
	}

	public void setCurrentRow(long currentRow) {
		this.currentRow = currentRow;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public DatasetConfiguration getDatasetConfiguration() {
		return datasetConfiguration;
	}

	public void setDatasetConfiguration(DatasetConfiguration datasetConfiguration) {
		this.datasetConfiguration = datasetConfiguration;
	}

	public Dataset getParentDataset() {
		return parentDataset;
	}

	public void setParentDataset(Dataset parentDataset) {
		this.parentDataset = parentDataset;
	}

	public DatasetType getDatasetType() {
		return datasetType;
	}

	public void setDatasetType(DatasetType datasetType) {
		this.datasetType = datasetType;
	}

}

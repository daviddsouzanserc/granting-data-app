package ca.gc.tri_agency.granting_data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import ca.gc.tri_agency.granting_data.model.Dataset;
import ca.gc.tri_agency.granting_data.model.Dataset.DatasetStatus;
import ca.gc.tri_agency.granting_data.model.Dataset.DatasetType;
import ca.gc.tri_agency.granting_data.model.DatasetConfiguration;
import ca.gc.tri_agency.granting_data.service.DatasetService;

@Service
public class DatasetRowMapper implements RowMapper<Dataset> {
	
	DatasetService datasetService;
	
	@Override
	public Dataset mapRow(ResultSet rs, int rowNum) throws SQLException {
		Dataset dataset = new Dataset();
		
		dataset.setId(rs.getLong("id"));
		dataset.setFilename(rs.getString("filename"));
		dataset.setParentDataset(datasetService.getDataset(rs.getLong("parent_dataset_id")));
		dataset.setDatasetConfiguration(datasetService.getDatasetConfiguration(rs.getLong("dataset_configuration_id")));
		dataset.setCreateDateTime(LocalDateTime.of(rs.getDate("create_date_time").toLocalDate(), rs.getTime("create_date_time").toLocalTime()));
		dataset.setUpdateDateTime(LocalDateTime.of(rs.getDate("update_date_time").toLocalDate(), rs.getTime("update_date_time").toLocalTime()));
		dataset.setTotalRecords(rs.getLong("total_records"));
		dataset.setCurrentRow(rs.getLong("current_row"));
		dataset.setDatasetStatus(DatasetStatus.valueOf(rs.getString("dataset_status")));
		dataset.setDatasetType(DatasetType.valueOf(rs.getString("dataset_type")));
		
		return dataset;
	}
	
}

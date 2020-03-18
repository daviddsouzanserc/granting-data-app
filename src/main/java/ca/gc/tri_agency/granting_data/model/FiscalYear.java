package ca.gc.tri_agency.granting_data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
public class FiscalYear implements LocalizedParametersModel {

	@Id
	@SequenceGenerator(name = "SEQ_FISCAL_YEAR", sequenceName = "SEQ_FISCAL_YEAR", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FISCAL_YEAR")
	private Long id;

	@NotNull(message = "The year should not be Null")
	@Min(1999)
	@Max(2050)
	@Column(name = "year", unique = true)
	private Long Year;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getYear() {
		return Year;
	}

	public void setYear(Long year) {
		Year = year;
	}

}

package ca.gc.tri_agency.granting_data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GrantingStage {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "granting_function")
	private String grantingFunction;

	public GrantingStage() {

	}

	public Long getId() {
		return id;
	}

	public String getGrantingFunction() {
		return grantingFunction;
	}

	public void setGrantingFunction(String grantingFunction) {
		this.grantingFunction = grantingFunction;
	}

}

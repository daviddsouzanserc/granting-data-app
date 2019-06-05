package ca.gc.tri_agency.granting_data.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class RegisteredDataset {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nameEn;

	private String nameFr;

	private String acronym;

	private String fileIdentifer;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable
	private Set<Program> targetedPrograms;

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

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getFileIdentifer() {
		return fileIdentifer;
	}

	public void setFileIdentifer(String fileIdentifer) {
		this.fileIdentifer = fileIdentifer;
	}

	public Set<Program> getTargetedPrograms() {
		return targetedPrograms;
	}

	public void setTargetedPrograms(Set<Program> targetedPrograms) {
		this.targetedPrograms = targetedPrograms;
	}

	public Integer getId() {
		return id;
	}

}

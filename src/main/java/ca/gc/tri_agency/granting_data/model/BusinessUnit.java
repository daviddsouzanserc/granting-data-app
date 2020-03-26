package ca.gc.tri_agency.granting_data.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;

@Entity
public class BusinessUnit implements LocalizedParametersModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(min = 3, max = 255)
    @Column(unique = true)
    private String nameEn;
    
    @NotBlank
    @Size(min = 3, max = 255)
    @Column(unique = true)
    private String nameFr;
    
    @NotBlank
    @Column(unique = true)
    private String acronymEn;
    
    @NotBlank
    @Column(unique = true)
    private String acronymFr;
    
    @ManyToOne
    private Set<FundingOpportunity> fos = new HashSet<>();
    
    @ManyToOne
    private Set<Agency> agencies = new HashSet<>();

//    @ManyToOne
//    private Set<MembershipAccess> membershipAccesses = new HashSet<>();
    
	public BusinessUnit(@NotBlank @Size(min = 3, max = 255) String nameEn,
			@NotBlank @Size(min = 3, max = 255) String nameFr,
			@NotBlank String acronymEn, @NotBlank String acronymFr) {
		this.nameEn = nameEn;
		this.nameFr = nameFr;
		this.acronymEn = acronymEn;
		this.acronymFr = acronymFr;
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

	public String getAcronymEn() {
		return acronymEn;
	}

	public void setAcronymEn(String acronymEn) {
		this.acronymEn = acronymEn;
	}

	public String getAcronymFr() {
		return acronymFr;
	}

	public void setAcronymFr(String acronymFr) {
		this.acronymFr = acronymFr;
	}

	public Long getId() {
		return id;
	}

	public Set<FundingOpportunity> getFos() {
		return fos;
	}

	public Set<Agency> getAgencies() {
		return agencies;
	}
    
}





















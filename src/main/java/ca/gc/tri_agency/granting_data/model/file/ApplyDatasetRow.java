package ca.gc.tri_agency.granting_data.model.file;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ebay.xcelite.annotations.Column;

@Entity
@Table(name = "master_dataset")
public class ApplyDatasetRow {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "Source")
	private String source;

	@Column(name = "Application_Identifier")
	private String applicationIdentifier;

	@Column(name = "Appl_ID")
	private long applId;
	
	private long datasetId;

	@Column(name = "Competition_Year")
	private int competitionYear;

	@Column(name = "Program_ID")
	private String programId;

	@Column(name = "Program_English")
	private String programEn;

	@Column(name = "Program_French")
	private String programFr;

	@Column(name = "Create_Date")
	private String createDate;

	@Column(name = "Role_Code")
	private int roleCode;

	@Column(name = "Role_English")
	private String roleEn;

	@Column(name = "Role_French")
	private String roleFr;

	@Column(name = "Family_Name")
	private String familyName;

	@Column(name = "Given_Name")
	private String givenName;

	@Column(name = "Person_Identifier")
	private long personIdentifier;

	@Column(name = "Organization_ID")
	private long orgId;

	@Column(name = "Organization_Name_English")
	private String orgNameEn;
	
	@Column(name = "Organization_Name_French")
	private String orgNameFr;
	//***************************
	@Column(name = "Address_1")
	private String addrOne;

	@Column(name = "Address_2")
	private String addrTwo;
	
	@Column(name = "Address_3")
	private String addrThree;
	
	@Column(name = "Address_4")
	private String addrFour;
	//***************************
	@Column(name = "Municipality")
	private String city;

	@Column(name = "Postal_Zip_Code")
	private String postalZipCode;

	@Column(name = "Province_State_Code")
	private String stateProvCode;
	//***************************
	@Column(name = "Country")
	private String country;
	//***************************
	
	public long getId() {
		return id;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getApplicationIdentifier() {
		return applicationIdentifier;
	}

	public void setApplicationIdentifier(String applicationIdentifier) {
		this.applicationIdentifier = applicationIdentifier;
	}

	public long getApplId() {
		return applId;
	}

	public void setApplId(long applId) {
		this.applId = applId;
	}

	public int getCompetitionYear() {
		return competitionYear;
	}

	public void setCompetitionYear(int competitionYear) {
		this.competitionYear = competitionYear;
	}

	public String getProgramEn() {
		return programEn;
	}

	public void setProgramEn(String programEn) {
		this.programEn = programEn;
	}

	public String getProgramFr() {
		return programFr;
	}

	public void setProgramFr(String programFr) {
		this.programFr = programFr;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(int roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleEn() {
		return roleEn;
	}

	public void setRoleEn(String roleEn) {
		this.roleEn = roleEn;
	}

	public String getRoleFr() {
		return roleFr;
	}

	public void setRoleFr(String roleFr) {
		this.roleFr = roleFr;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

//	public void fixPersonIdentifier() {
//		int dotLocation = personIdentifier.indexOf(".");
//		if (dotLocation > 0) {
//			personIdentifier = personIdentifier.substring(0, dotLocation);
//		}
//	}
	public long getPersonIdentifier() {
		return personIdentifier;
	}

	public void setPersonIdentifier(long personIdentifier) {
		this.personIdentifier = personIdentifier;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

//	public void fixOrgId() {
//		int dotLocation = orgId.indexOf(".");
//		if (dotLocation > 0) {
//			orgId = orgId.substring(0, dotLocation);
//		}
//	}
//	public void fixApplId() {
//		int dotLocation = applId.indexOf(".");
//		if (dotLocation > 0) {
//			applId = applId.substring(0, dotLocation);
//		}
//	}

	public String getOrgNameEn() {
		return orgNameEn;
	}

	public void setOrgNameEn(String orgNameEn) {
		this.orgNameEn = orgNameEn;
	}

	public String getOrgNameFr() {
		return orgNameFr;
	}

	public void setOrgNameFr(String orgNameFr) {
		this.orgNameFr = orgNameFr;
	}
	//***************************
	public String getAddrOne() {
		return addrOne;
	}

	public void setAddrOne(String addrOne) {
		this.addrOne = addrOne;
	}
	public String getAddrTwo() {
		return addrTwo;
	}

	public void setAddrTwo(String addrTwo) {
		this.addrTwo = addrTwo;
	}
	public String getAddrThree() {
		return addrThree;
	}

	public void setAddrThree(String addrThree) {
		this.addrThree = addrThree;
	}
	public String getAddrFour() {
		return addrFour;
	}

	public void setAddrFour(String addrFour) {
		this.addrFour = addrFour;
	}
	//***************************
	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
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
	//***************************
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	//***************************

	public long getDatasetId() {
		return datasetId;
	}

	public void setDatasetId(long datasetId) {
		this.datasetId = datasetId;
	}
}

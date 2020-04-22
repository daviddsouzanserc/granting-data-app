package ca.gc.tri_agency.granting_data.model;

import java.time.Instant;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;

@Entity
public class ApplicationParticipation {

    @Id
    @SequenceGenerator(name = "SEQ_APPLICATION_PARTICIPATION", sequenceName = "SEQ_APPLICATION_PARTICIPATION", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_APPLICATION_PARTICIPATION")
    private Long id;

    private String applicationIdentifier;

    private Long applicationId;

    private Long competitionYear;

    private String programId;

    private String programEn;

    private String programFr;

    @CreatedDate
    private Instant createDate;

    private Long roleCode;

    private String roleEn;

    private String roleFr;

    private String familyName;

    private String givenName;

    private Long personIdentifier;

    private Long organizationId;

    private String organizationNameEn;

    private String organizationNameFr;

    @Column(name = "freeform_address_1")
    private String freeformAddress1;

    @Column(name = "freeform_address_2")
    private String freeformAddress2;

    @Column(name = "freeform_address_3")
    private String freeformAddress3;

    @Column(name = "freeform_address_4")
    private String freeformAddress4;

    private String municipality;

    @Size(max = 7)
    private String postalZipCode;

    @Size(max = 3)
    private String provinceStateCode;

    private String country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicationIdentifier() {
        return applicationIdentifier;
    }

    public void setApplicationIdentifier(String applicationIdentifier) {
        this.applicationIdentifier = applicationIdentifier;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getCompetitionYear() {
        return competitionYear;
    }

    public void setCompetitionYear(Long competitionYear) {
        this.competitionYear = competitionYear;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
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

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Long getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(Long roleCode) {
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

    public Long getPersonIdentifier() {
        return personIdentifier;
    }

    public void setPersonIdentifier(Long personIdentifier) {
        this.personIdentifier = personIdentifier;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationNameEn() {
        return organizationNameEn;
    }

    public void setOrganizationNameEn(String organizationNameEn) {
        this.organizationNameEn = organizationNameEn;
    }

    public String getOrganizationNameFr() {
        return organizationNameFr;
    }

    public void setOrganizationNameFr(String organizationNameFr) {
        this.organizationNameFr = organizationNameFr;
    }

    public String getFreeformAddress1() {
        return freeformAddress1;
    }

    public void setFreeformAddress1(String freeformAddress1) {
        this.freeformAddress1 = freeformAddress1;
    }

    public String getFreeformAddress2() {
        return freeformAddress2;
    }

    public void setFreeformAddress2(String freeformAddress2) {
        this.freeformAddress2 = freeformAddress2;
    }

    public String getFreeformAddress3() {
        return freeformAddress3;
    }

    public void setFreeformAddress3(String freeformAddress3) {
        this.freeformAddress3 = freeformAddress3;
    }

    public String getFreeformAddress4() {
        return freeformAddress4;
    }

    public void setFreeformAddress4(String freeformAddress4) {
        this.freeformAddress4 = freeformAddress4;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getPostalZipCode() {
        return postalZipCode;
    }

    public void setPostalZipCode(String postalZipCode) {
        this.postalZipCode = postalZipCode;
    }

    public String getProvinceStateCode() {
        return provinceStateCode;
    }

    public void setProvinceStateCode(String provinceStateCode) {
        this.provinceStateCode = provinceStateCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof ApplicationParticipation))
            return false;
        ApplicationParticipation other = (ApplicationParticipation) obj;
        return Objects.equals(applicationId, other.applicationId) && Objects.equals(applicationIdentifier, other.applicationIdentifier) && Objects.equals(competitionYear, other.competitionYear) && Objects.equals(country, other.country)
            && Objects.equals(createDate, other.createDate) && Objects.equals(familyName, other.familyName) && Objects.equals(freeformAddress1, other.freeformAddress1) && Objects.equals(freeformAddress2, other.freeformAddress2)
            && Objects.equals(freeformAddress3, other.freeformAddress3) && Objects.equals(freeformAddress4, other.freeformAddress4) && Objects.equals(givenName, other.givenName) && Objects.equals(municipality, other.municipality)
            && Objects.equals(organizationNameEn, other.organizationNameEn) && Objects.equals(organizationId, other.organizationId) && Objects.equals(organizationNameFr, other.organizationNameFr) && Objects.equals(personIdentifier, other.personIdentifier)
            && Objects.equals(postalZipCode, other.postalZipCode) && Objects.equals(programEn, other.programEn) && Objects.equals(programFr, other.programFr) && Objects.equals(programId, other.programId)
            && Objects.equals(provinceStateCode, other.provinceStateCode) && Objects.equals(roleCode, other.roleCode) && Objects.equals(roleEn, other.roleEn) && Objects.equals(roleFr, other.roleFr);
    }


}

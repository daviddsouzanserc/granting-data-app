package applicationparticipationintegrationtest;

import static org.junit.Assert.assertEquals;

import java.time.Instant;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ca.gc.tri_agency.granting_data.app.GrantingDataApp;
import ca.gc.tri_agency.granting_data.model.ApplicationParticipation;
import ca.gc.tri_agency.granting_data.repo.ApplicationParticipationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrantingDataApp.class)
@ActiveProfiles("local")
public class BrowseApplicationParticipationIntegrationTest {

    @Autowired
    private ApplicationParticipationRepository apRepo;

    @Test
    public void test_repo() {
        long initApRepoCount = apRepo.count();
        
        ApplicationParticipation ap = new ApplicationParticipation();
        ap.setApplicationIdentifier(RandomStringUtils.randomAlphabetic(10));
        ap.setCompetitionYear(2022L);
        ap.setCountry(RandomStringUtils.randomAlphabetic(10));
        
        Instant currentTimestamp = Instant.now();
        ap.setCreateDate(currentTimestamp);
        
        ap.setFamilyName(RandomStringUtils.randomAlphabetic(10));
        ap.setFreeformAddress1(RandomStringUtils.randomAlphabetic(10));
        ap.setFreeformAddress2(RandomStringUtils.randomAlphabetic(10));
        ap.setFreeformAddress3(RandomStringUtils.randomAlphabetic(10));
        ap.setFreeformAddress4(RandomStringUtils.randomAlphabetic(10));
        ap.setGivenName(RandomStringUtils.randomAlphabetic(10));
        ap.setMunicipality(RandomStringUtils.randomAlphabetic(10));
        ap.setOrganizationNameEn(RandomStringUtils.randomAlphabetic(10));
        ap.setOrganizationNameFr(RandomStringUtils.randomAlphabetic(10));
        ap.setOrganizationId(1L);
        ap.setPersonIdentifier(99L);
        ap.setPostalZipCode(RandomStringUtils.randomAlphabetic(7));
        ap.setProgramEn(RandomStringUtils.randomAlphabetic(10));
        ap.setProgramFr(RandomStringUtils.randomAlphabetic(10));
        ap.setProgramId(RandomStringUtils.randomAlphabetic(10));
        ap.setProvinceStateCode(RandomStringUtils.randomAlphabetic(2));
        ap.setRoleCode(1234L);
        ap.setRoleEn(RandomStringUtils.randomAlphabetic(10));
        ap.setRoleFr(RandomStringUtils.randomAlphabetic(10));

        apRepo.save(ap);
        assertEquals(initApRepoCount + 1, apRepo.count());
        assertEquals(ap, apRepo.findById((long) apRepo.count()).get());
        assertEquals(currentTimestamp, apRepo.findById((long) apRepo.count()).get().getCreateDate());
    }
}

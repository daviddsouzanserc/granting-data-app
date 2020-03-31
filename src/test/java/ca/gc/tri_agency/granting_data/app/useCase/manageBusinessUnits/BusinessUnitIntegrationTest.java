package ca.gc.tri_agency.granting_data.app.useCase.manageBusinessUnits;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import ca.gc.tri_agency.granting_data.app.GrantingDataApp;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrantingDataApp.class)
@Profile("test")
public class BusinessUnitIntegrationTest {

    // CREATE TEST THAT VERIFIES THAT ANYONE CAN VIEW ALL OF THE BUSINESS UNITS
    
    // CREATE TEST THAT VERIFIES THAT AN ADMIN USER CAN CREATE A BUSINESS UNIT
    
    // CREATE TEST THAT VERIFIES THAT A NON-ADMIN USER CANNOT CREATE A BUSINESS UNIT
    
    // CREATE TEST THAT VERIFIES THAT AN ADMIN USER CAN UPDATE A BUSINESS UNIT
    
    // CREATE TEST THAT VERIFIES THAT A NON-ADMIN USER CANNOT UPDATE A BUSINESS UNIT
}

package ca.gc.tri_agency.granting_data.app.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ca.gc.tri_agency.granting_data.app.GrantingDataApp;
import ca.gc.tri_agency.granting_data.model.ldap.ADUser;
import ca.gc.tri_agency.granting_data.repo.ADUserRepository;

@SpringBootTest(classes = GrantingDataApp.class)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class SpringLdapIntegrationTest {

	@Autowired
	private ADUserRepository userRepository;

	@Test
	public void testGetAllPersons() {
		List<ADUser> users = userRepository.getAllPersons();
		assertNotNull(users);
		assertEquals(users.size(), 5);
	}

	@Test
	public void testGetAllPersonsNames() {
		List<String> persons = userRepository.getAllPersonNames();
		assertNotNull(persons);
		assertEquals(persons.size(), 5);
	}

	@Test
	public void testFindPerson() {
		ADUser user = userRepository.findPerson("uid=admin,ou=people,dc=nserc_poc,dc=net");
		assertNotNull(user);
		assertEquals(user.getFullName(), "Admin User");
	}
}
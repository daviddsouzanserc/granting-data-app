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
import ca.gc.tri_agency.granting_data.repoLdap.ADUserRepository;

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
		assertEquals(6, users.size());
	}

	@Test
	public void testGetAllPersonsNames() {
		List<String> persons = userRepository.getAllPersonNames();
		assertNotNull(persons);
		assertEquals(6, persons.size());
	}

	@Test
	public void testFindNSERCPerson() {
		ADUser user = userRepository.findPerson("uid=admin,ou=NSERC_Users");
		assertNotNull(user);
		assertEquals(user.getFullName(), "Admin User");
	}

	@Test
	public void testFindSSHRCPerson() {
		ADUser user = userRepository.findPerson("uid=sshrc-admin,ou=SSHRC_Users");
		assertNotNull(user);
		assertEquals("SSHRC Admin", user.getFullName());
	}

}
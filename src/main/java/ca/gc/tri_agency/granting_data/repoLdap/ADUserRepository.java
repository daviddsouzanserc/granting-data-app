package ca.gc.tri_agency.granting_data.repoLdap;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import ca.gc.tri_agency.granting_data.model.ldap.ADUser;

@Service
public class ADUserRepository {

	@Autowired
	private LdapTemplate ldapTemplate;

	public List<String> getAllPersonNames() {
		return ldapTemplate.search(query().where("objectclass").is("person"), new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs) throws NamingException {
				return (String) attrs.get("cn").get();
			}
		});
	}

	public List<ADUser> getAllPersons() {
		return ldapTemplate.search(query().where("objectclass").is("person"), new PersonAttributesMapper());
	}

	public ADUser findPerson(String dn) {
		return ldapTemplate.lookup(dn, new PersonAttributesMapper());
	}

	private class PersonAttributesMapper implements AttributesMapper<ADUser> {
		public ADUser mapFromAttributes(Attributes attrs) throws NamingException {
			ADUser person = new ADUser();
			person.setFullName((String) attrs.get("cn").get());
			person.setLastName((String) attrs.get("sn").get());
			return person;
		}
	}
}
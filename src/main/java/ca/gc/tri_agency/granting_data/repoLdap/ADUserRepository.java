package ca.gc.tri_agency.granting_data.repoLdap;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import ca.gc.tri_agency.granting_data.model.ldap.ADUser;

@Service
public class ADUserRepository {

	@Autowired
	@Qualifier("ldapTemplateNSERC")
	private LdapTemplate ldapTemplateNSERC;

	@Autowired
	@Qualifier("ldapTemplateSSHRC")
	private LdapTemplate ldapTemplateSSHRC;

	public List<String> getAllPersonNames() {
		List<String> retval = ldapTemplateNSERC.search(query().where("objectclass").is("person"),
				new AttributesMapper<String>() {
					public String mapFromAttributes(Attributes attrs) throws NamingException {
						return (String) attrs.get("cn").get();
					}
				});
		retval.addAll(
				ldapTemplateSSHRC.search(query().where("objectclass").is("person"), new AttributesMapper<String>() {
					public String mapFromAttributes(Attributes attrs) throws NamingException {
						return (String) attrs.get("cn").get();
					}
				}));
		return retval;
	}

	public List<ADUser> getAllPersons() {
		List<ADUser> retval = ldapTemplateNSERC.search(query().where("objectclass").is("person"),
				new PersonAttributesMapper());
		retval.addAll(
				ldapTemplateSSHRC.search(query().where("objectclass").is("person"), new PersonAttributesMapper()));
		return retval;
	}

	public ADUser findPerson(String dn) {
		// consider changing name to lookup, and create another findPerson function
		ADUser retval = null;
		try {
			retval = ldapTemplateNSERC.lookup(dn, new PersonAttributesMapper());
		} catch (Exception e) {
			//

		}
		if (retval == null) {
			retval = ldapTemplateSSHRC.lookup(dn, new PersonAttributesMapper());
		}
		return retval;
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
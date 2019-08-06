package ca.gc.tri_agency.granting_data.repoLdap;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import ca.gc.tri_agency.granting_data.model.User;

@Service
public class UserRepo {

	@Autowired
	private LdapTemplate ldapTemplate;

	// used search method
	public List<User> searchOther(String username) {

		SearchControls sc = new SearchControls();
		sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
		// sc.setReturningAttributes(null); // new String[] { "cn" } <- if want only
		// specific attributes. null if want all
		// attributes. empty array if want no attributes
		sc.setReturningAttributes(new String[] { "cn", "sn", "dn" });

		String filter = "(&(objectclass=person)(cn=" + username + "))";

		return ldapTemplate.search(LdapUtils.emptyLdapName(), filter, sc, new UserAttributesMapper());
	}

	public User findPerson(String dn) {
		return ldapTemplate.lookup(dn, new PersonAttributesMapper());
	}

	private class PersonAttributesMapper implements AttributesMapper<User> {
		public User mapFromAttributes(Attributes attrs) throws NamingException {
			User person = new User();
			person.setUsername((String) attrs.get("cn").get());
			person.setSn((String) attrs.get("sn").get());
			return person;
		}
	}

	private class UserAttributesMapper implements AttributesMapper<User> {

		@Override
		public User mapFromAttributes(Attributes attributes) throws NamingException {
			User user = new User();

			Attribute cn = attributes.get("cn");
			if (cn != null) {
				user.setUsername((String) cn.get());
			}
			Attribute sn = attributes.get("sn");
			if (sn != null) {
				user.setSn((String) sn.get());
			}
			Attribute dn = attributes.get("dn");
			if (dn == null) {
				System.out.println("null dn");
			}
			if (dn != null) {
				user.setDn((String) dn.get());
			}

			return user;
		}

	}
}

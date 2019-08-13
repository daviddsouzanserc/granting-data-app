package ca.gc.tri_agency.granting_data.repoLdap;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import ca.gc.tri_agency.granting_data.model.User;

@Service
public class UserRepo {

	@Value("${ldap.base.dn.nserc}")
	private String nsercBaseDn;

	@Value("${ldap.base.dn.sshrc}")
	private String sshrcBaseDn;

	private String nsercOu = "NSERC_Users";
	private String sshrcOu = "SSHRC1_Users";

	@Autowired
	private LdapTemplate ldapTemplate;

	// used search method
	public List<User> searchOther(String username) {

		SearchControls sc = new SearchControls();
		sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
		// sc.setSearchScope(SearchControls.OBJECT_SCOPE);
		// sc.setSearchScope(SearchControls.ONELEVEL_SCOPE);

		sc.setReturningAttributes(null); // new String[] { "cn" } <- if want only
		// specific attributes. null if want all
		// attributes. empty array if want no attributes
		// sc.setReturningAttributes(new String[] { "cn", "sn", "dn" });

		String filter = "(&(objectclass=person)(cn=" + username + "))";

		return ldapTemplate.search(LdapUtils.emptyLdapName(), filter, sc, new UserAttributesMapper());
	}

	public User findPerson(String dn) {
		return ldapTemplate.lookup(dn, new PersonAttributesMapper());
	}

	private String buildDn(User user) {
		try {
			return LdapNameBuilder.newInstance(nsercBaseDn).add("ou", nsercOu).add("uid", user.getUid()).build()
					.toString();
		} catch (Exception e) {
			return null;
		}

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
			Attribute uid = attributes.get("uid");
			if (uid != null) {
				user.setUid((String) uid.get());
			}
			// Attribute dn = attributes.get("dn");
			String dn = buildDn(user);

			if (dn != null) {
				// user.setDn((String) dn.get());
				user.setDn(dn);
			}

			return user;
		}

	}
}

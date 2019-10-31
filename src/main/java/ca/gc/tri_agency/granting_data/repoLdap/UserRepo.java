package ca.gc.tri_agency.granting_data.repoLdap;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.WhitespaceWildcardsFilter;
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

	public String getDnByUsername(String username) {

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "person"));
		filter.and(new WhitespaceWildcardsFilter("cn", username));

		List<Object> result = ldapTemplate.search("", filter.toString(), new AbstractContextMapper<Object>() {
			@Override
			protected Object doMapFromContext(DirContextOperations ctx) {
				return ctx.getNameInNamespace();
			}
		});
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0).toString();

	}

	public List<User> searchOther(String username) {

		SearchControls sc = new SearchControls();
		sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

		sc.setReturningAttributes(null); // new String[] { "cn" } <- if want only
		// specific attributes. null if want all
		// attributes. empty array if want no attributes
		// sc.setReturningAttributes(new String[] { "cn", "sn", "dn" });

		String filter = "(&(objectclass=person)(cn=" + username + "))";

		return ldapTemplate.search(LdapUtils.emptyLdapName(), filter, sc, new UserAttributesMapper());
	}

	public User findPerson(String dn) {
		return ldapTemplate.lookup(dn, new UserAttributesMapper());
	}

	public List<User> getAllPersons() {
		return ldapTemplate.search(query().where("objectclass").is("person"), new UserAttributesMapper());
	}

//	private String buildDn(User user) {
//		try {
//			return LdapNameBuilder.newInstance(nsercBaseDn).add("ou", nsercOu).add("uid", user.getUid()).build()
//					.toString();
//		} catch (Exception e) {
//			return null;
//		}
//
//	}

	private String buildDn(String username) {
		String dn = null;

		dn = getDnByUsername(username);
		return dn;
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
			String dn = buildDn((String) cn.get());

			if (dn != null) {
				// user.setDn((String) dn.get());
				user.setDn(dn);
			}

			Attribute mail = attributes.get("mail");
			if (mail != null) {
				user.setMail((String) mail.get());
			}

			return user;
		}

	}
}

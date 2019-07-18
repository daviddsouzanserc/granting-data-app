package ca.gc.tri_agency.granting_data.service;

import java.util.List;
import java.util.stream.Collectors;

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

	public List<User> searchOther(String username) {

		SearchControls sc = new SearchControls();
		sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
		sc.setReturningAttributes(null); // new String[] { "cn" } <- if want only specific attributes. null if want all
											// attributes. empty array if want no attributes

		String filter = "(&(objectclass=person)(cn=" + username + "))";

		return ldapTemplate.search(LdapUtils.emptyLdapName(), filter, sc, new UserAttributesMapper());
	}

	public List<String> search(String username) {

		SearchControls sc = new SearchControls();
		sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
		sc.setReturningAttributes(new String[] { "cn" });

		String filter = "(&(objectclass=person)(cn=" + username + "))";

		return ldapTemplate.search(LdapUtils.emptyLdapName(), filter, sc, new UserAttributesMapper()).stream()
				.map(User::getUsername).collect(Collectors.toList());
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

			return user;
		}

	}
}

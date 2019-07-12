package ca.gc.tri_agency.granting_data.service.impl;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.stereotype.Service;

import ca.gc.tri_agency.granting_data.model.User;
import ca.gc.tri_agency.granting_data.repoLdap.UserRepository;
import ca.gc.tri_agency.granting_data.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	private LdapTemplate ldapTemplate;

	public List<String> search(String u) {
		User userList = userRepository.findByUsername(u);
//		List<User> userList = userRepository.findByUsernameLikeIgnoreCase(u);

		if (userList == null) {
			return Collections.emptyList();
		}

//		return userList.stream().map(User::getUsername).collect(Collectors.toList());
		List<String> retval = new ArrayList<String>();
		retval.add(userList.getUsername());
		return retval;
	}

	@Override
	public List<String> getAllUsers() {
		LdapQuery query = query().base("ou=NSERC_Users,dc=nserc,dc=ca").attributes("cn", "sn").where("objectclass")
				.is("person");

		return ldapTemplate.search(query, new AttributesMapper<String>() {
			public String mapFromAttributes(javax.naming.directory.Attributes attrs) throws NamingException {
				return (String) attrs.get("cn").get();
			}
		});
	}

}

package ca.gc.tri_agency.granting_data.repoLdap;

import java.util.List;

import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

import ca.gc.tri_agency.granting_data.model.User;

@Repository
public interface UserRepository extends LdapRepository<User>{
	User findByUsername(String username);
	User findByUsernameAndPassword(String username, String password);
	List<User> findByUsernameLikeIgnoreCase(String username);
	User findByAccountName(String accountName);
}

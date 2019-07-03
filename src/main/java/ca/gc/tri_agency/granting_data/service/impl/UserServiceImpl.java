package ca.gc.tri_agency.granting_data.service.impl;

import ca.gc.tri_agency.granting_data.model.User;
import ca.gc.tri_agency.granting_data.repo.UserRepository;
import ca.gc.tri_agency.granting_data.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public List<String> search(String u){
		List<User> userList = userRepository.findByUsernameLikeIgnoreCase(u);
		
		if( userList == null) {
			return Collections.emptyList();
		}
		
		return userList.stream().map(User::getUsername).collect(Collectors.toList());
	}
}

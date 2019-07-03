package ca.gc.tri_agency.granting_data.service;

import ca.gc.tri_agency.granting_data.model.User;
import ca.gc.tri_agency.granting_data.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public interface UserService {
	
	public List<String> search(String u);
}

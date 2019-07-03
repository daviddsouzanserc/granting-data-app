package ca.gc.tri_agency.granting_data.model;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;


@Entry(
	base = "ou=users"/* or should be "ou=people" */,	
	objectClasses = { /* "person", "inetOrgPerson", "top"  */ }	)
public class User {
	@Id
	private Name id;

	private @Attribute(name = "cn") String username;
	private @Attribute(name = "sn") String password;
	
	public Name getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setId(Name id) {
		this.id = id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
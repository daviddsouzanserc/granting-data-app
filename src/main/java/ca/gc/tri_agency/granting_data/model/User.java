package ca.gc.tri_agency.granting_data.model;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;


@Entry(base = "ou=NSERC_Users",
objectClasses = {"top","organizationalUnit","person","user"}) 
public final class User {
	@Id
	private Name id;

	private @Attribute(name = "cn") String username;
	private @Attribute(name = "sn") String password;
	private @Attribute(name = "sAMAccountName") String accountName;
	
	public Name getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getAccountName() {
		return accountName;
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
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	
	public String toString() {
		return username + "\t" + accountName;
	}
}
package ca.gc.tri_agency.granting_data.model;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

@Entry(base = "ou=NSERC_Users", objectClasses = { "top", "organizationalUnit", "person", "user" })
public final class User {
	@Id
	private Name id;

	private @Attribute(name = "cn") String username;
	private @Attribute(name = "sn") String sn;
	private @Attribute(name = "sAMAccountName") String accountName;
	private @Attribute(name = "dn") String dn;
	private @Attribute(name = "uid") String uid;
	private @Attribute(name = "mail") String mail;

	public String getMail() {
		return mail;
	}

	public Name getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getSn() {
		return sn;
	}

	public String getAccountName() {
		return accountName;
	}

	public String getDn() {
		return dn;
	}

	public String getUid() {
		return uid;
	}

	public void setId(Name id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String toString() {
		return username + "\t" + accountName;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
}
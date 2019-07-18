package ca.gc.tri_agency.granting_data.model.ldap;

public class ADUser {

	private String email;

	private String fullName;
	private String lastName;

	public ADUser() {
	}

	public ADUser(String fullName, String lastName) {
		this.fullName = fullName;
		this.lastName = lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Person{" + "fullName='" + fullName + '\'' + ", lastName='" + lastName + '\'' + '}';
	}

}

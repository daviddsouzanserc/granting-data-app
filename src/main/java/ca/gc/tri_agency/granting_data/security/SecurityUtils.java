package ca.gc.tri_agency.granting_data.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

public class SecurityUtils {

	public static LdapUserDetails getLdapUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return null;
		}
		if (!auth.isAuthenticated() || auth.getPrincipal() instanceof String) {
			return null;
		}
		// Object principal = auth.getPrincipal();
		return (LdapUserDetails) auth.getPrincipal();

	}

	public static String getLdapUserDn() {
		return getLdapUser().getDn();

	}

	public static boolean hasRole(String role) {
		LdapUserDetails principal = getLdapUser();
		Collection<? extends GrantedAuthority> userAuthorities = principal.getAuthorities();
		for (GrantedAuthority g : userAuthorities) {
			if (g.getAuthority().equals("ROLE_" + role)) {
				return true;
			}
		}
		return false;

	}

	// boolean validateUserRole(String role) {
	// boolean retval = false;
	//
	// LdapUserDetails principal = getLdapUser();
	// Collection<? extends GrantedAuthority> userAuthorities =
	// principal.getAuthorities();
	// for (GrantedAuthority g : userAuthorities) {
	// if (g.getAuthority().equals("ROLE_ADMIN")) {
	// // logged in user is
	// // an admin
	// return true;
	// }
	// }
	//
	// return retval;
	//
	// }

}

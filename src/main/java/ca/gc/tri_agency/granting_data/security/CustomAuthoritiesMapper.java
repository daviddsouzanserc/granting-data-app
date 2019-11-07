package ca.gc.tri_agency.granting_data.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthoritiesMapper implements GrantedAuthoritiesMapper {

	private GrantedAuthority defaultAuthority;

	@Override
	public Set<GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {

		HashSet<GrantedAuthority> mapped = new HashSet<>();

		for (GrantedAuthority authority : authorities) {
			// check if user is a member of app admin group
			if (authority.getAuthority().equals("DEV_APP_CRM")) { // Change "DEV_APP_CRM" to actual admin group
				mapped.add(mapAuthority("MDM ADMIN"));
			}
		}
		// Add default authority if applicable
		if (defaultAuthority != null) {
			mapped.add(defaultAuthority);
		}

		return mapped;
	}

	private GrantedAuthority mapAuthority(String name) {
		return new SimpleGrantedAuthority("ROLE_" + name);
	}

	public void setDefaultAuthority(String name) {
		this.defaultAuthority = mapAuthority(name);
	}

}

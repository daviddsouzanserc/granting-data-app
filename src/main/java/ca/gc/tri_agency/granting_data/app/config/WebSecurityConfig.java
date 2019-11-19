package ca.gc.tri_agency.granting_data.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

import ca.gc.tri_agency.granting_data.security.CustomAuthoritiesMapper;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
	@Value("${ldap.url.nserc}")
	private String ldapUrlNSERC;

	@Value("${ldap.base.dn.nserc}")
	private String ldapBaseDnNSERC;

	@Value("${ldap.domain.nserc}")
	private String ldapDomainNSERC;

	@Value("${ldap.url.sshrc}")
	private String ldapUrlSSHRC;

	@Value("${ldap.base.dn.sshrc}")
	private String ldapBaseDnSSHRC;

	@Value("${ldap.domain.sshrc}")
	private String ldapDomainSSHRC;

	@Value("${ldap.group.search.base}")
	private String ldapGroupSearchBase;

	@Value("${ldap.user.dn.pattern}")
	private String ldapUserDnPattern;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// auth.authenticationProvider(activeDirectoryLdapAuthenticationProviderNSERC());
		// auth.authenticationProvider(activeDirectoryLdapAuthenticationProviderSSHRC());
		auth.ldapAuthentication().userDnPatterns(ldapUserDnPattern).groupSearchBase(ldapGroupSearchBase).contextSource()
				.url(ldapUrlNSERC + ldapBaseDnNSERC).and().passwordCompare().passwordAttribute("userPassword");

//		BaseLdapPathContextSource contextSource = getContextSource();
//		LdapAuthenticator ldapAuthenticator = createLdapAuthenticator(contextSource);
//
//		LdapAuthoritiesPopulator authoritiesPopulator = getLdapAuthoritiesPopulator();
//
//		LdapAuthenticationProvider ldapAuthenticationProvider = new LdapAuthenticationProvider(
//				ldapAuthenticator, authoritiesPopulator);
//		ldapAuthenticationProvider.setAuthoritiesMapper(getAuthoritiesMapper());
//		if (userDetailsContextMapper != null) {
//			ldapAuthenticationProvider
//					.setUserDetailsContextMapper(userDetailsContextMapper);
//		}
//		return ldapAuthenticationProvider;

	}

//	@Configuration
//	@Order(1)
//	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
//		protected void configure(HttpSecurity http) throws Exception {
//			http.antMatcher("/api/**").authorizeRequests().anyRequest()
//					.hasAnyRole("NSERC_USER", "SSHRC_USER", "AGENCY_USER","nserc-user-edi").anyRequest().authenticated().and()
//					.httpBasic();
//		}
//	}

	@Configuration
	@Order(1)
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.authorizeRequests()
					.antMatchers("/", "/home", "/webjars/**", "/css/**", "/images/**", "/js/**", "/browse/**")
					.permitAll().and().authorizeRequests().antMatchers("/entities/**", "/reports/**")
					.hasAnyRole("NSERC_USER", "SSHRC_USER", "AGENCY_USER").anyRequest().authenticated().and()
					.formLogin().loginPage("/login").permitAll().and().logout().permitAll().and().exceptionHandling()
					.accessDeniedPage("/exception/forbiden-by-role");

		}
	}

	/*
	 * @Bean public ProviderManager authenticationManager() { return new
	 * ProviderManager(Arrays.asList(
	 * activeDirectoryLdapAuthenticationProviderNSERC(),
	 * activeDirectoryLdapAuthenticationProviderSSHRC())); }
	 */
	private AuthenticationProvider activeDirectoryLdapAuthenticationProviderSSHRC() {
		ActiveDirectoryLdapAuthenticationProvider nsercProvider = new ActiveDirectoryLdapAuthenticationProvider(
				ldapDomainNSERC, ldapUrlNSERC, ldapBaseDnNSERC);
		CustomAuthoritiesMapper authMapper = new CustomAuthoritiesMapper();
		authMapper.setDefaultAuthority("NSERC_USER");
		nsercProvider.setConvertSubErrorCodesToExceptions(true);
		nsercProvider.setUseAuthenticationRequestCredentials(true);
		nsercProvider.setAuthoritiesMapper(authMapper);

		return nsercProvider;
	}

	private AuthenticationProvider activeDirectoryLdapAuthenticationProviderNSERC() {
		ActiveDirectoryLdapAuthenticationProvider sshrcProvider = new ActiveDirectoryLdapAuthenticationProvider(
				ldapDomainSSHRC, ldapUrlSSHRC, ldapBaseDnSSHRC);
		CustomAuthoritiesMapper authMapper = new CustomAuthoritiesMapper();
		authMapper.setDefaultAuthority("SSHRC_USER");
		sshrcProvider.setConvertSubErrorCodesToExceptions(true);
		sshrcProvider.setUseAuthenticationRequestCredentials(true);
		sshrcProvider.setAuthoritiesMapper(authMapper);

		return sshrcProvider;

//		ContextSourceBuilder contextSourceBuilder = new ContextSourceBuilder();
//		BaseLdapPathContextSource contextSource = getContextSource();
//		LdapAuthenticator ldapAuthenticator = createLdapAuthenticator(contextSource);
//
//		LdapAuthoritiesPopulator authoritiesPopulator = getLdapAuthoritiesPopulator();
//
//		LdapAuthenticationProvider ldapAuthenticationProvider = new LdapAuthenticationProvider(
//				ldapAuthenticator, authoritiesPopulator);
//		ldapAuthenticationProvider.setAuthoritiesMapper(getAuthoritiesMapper());
//		if (userDetailsContextMapper != null) {
//			ldapAuthenticationProvider
//					.setUserDetailsContextMapper(userDetailsContextMapper);
//		}
//		return ldapAuthenticationProvider;
	}
}

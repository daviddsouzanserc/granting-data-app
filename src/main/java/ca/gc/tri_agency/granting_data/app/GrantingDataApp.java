package ca.gc.tri_agency.granting_data.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

//import ca.gc.tri_agency.granting_data.service.ImportService;

@SpringBootApplication
@ComponentScan("ca.gc.tri_agency.granting_data")
@EntityScan("ca.gc.tri_agency.granting_data.model")
@EnableJpaRepositories(basePackages = { "ca.gc.tri_agency.granting_data.repo" })
@EnableLdapRepositories(basePackages = { "ca.gc.tri_agency.granting_data.repoLdap" })
@PropertySource("classpath:application.properties")
public class GrantingDataApp {

	@Autowired
	private Environment env;

	@Bean
	public LdapContextSource contextSource() {
		LdapContextSource contextSource = new LdapContextSource();

		contextSource.setUrl(env.getRequiredProperty("ldap.url.nserc"));
		// contextSource.setBase(env.getRequiredProperty("ldap.base.dn.nserc"));
		// contextSource.setUrls(new String[] { "ldap.url.nserc", "ldap.url.sshrc" });

		return contextSource;
	}

	@Bean
	public LdapTemplate ldapTemplate() {
		LdapTemplate l = new LdapTemplate(contextSource());
		l.setIgnorePartialResultException(true);
		return l;
	}

	public static void main(String[] args) {
		SpringApplication.run(GrantingDataApp.class, args);
	}
}

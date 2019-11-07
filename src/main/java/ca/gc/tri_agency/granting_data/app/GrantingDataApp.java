package ca.gc.tri_agency.granting_data.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;

//import ca.gc.tri_agency.granting_data.service.ImportService;

@SpringBootApplication
@ComponentScan("ca.gc.tri_agency.granting_data")
@EntityScan("ca.gc.tri_agency.granting_data.model")
@EnableJpaRepositories(basePackages = { "ca.gc.tri_agency.granting_data.repo" })
@EnableLdapRepositories(basePackages = { "ca.gc.tri_agency.granting_data.repoLdap" })
@PropertySource("classpath:application.properties")
public class GrantingDataApp {

//	@Autowired
//	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(GrantingDataApp.class, args);
	}
}

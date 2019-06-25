package ca.gc.tri_agency.granting_data.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//import ca.gc.tri_agency.granting_data.service.ImportService;

@SpringBootApplication
@ComponentScan("ca.gc.tri_agency.granting_data")
@EntityScan("ca.gc.tri_agency.granting_data.model")
@EnableJpaRepositories(basePackages = { "ca.gc.tri_agency.granting_data.repo" })
public class GrantingDataApp {
	
//	@Autowired
//	ImportService importService;
	
	
    public static void main( String[] args )
    {
    	SpringApplication.run(GrantingDataApp.class, args);
    }
}

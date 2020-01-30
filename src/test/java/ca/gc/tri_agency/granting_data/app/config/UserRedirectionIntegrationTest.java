package ca.gc.tri_agency.granting_data.app.config;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import ca.gc.tri_agency.granting_data.app.GrantingDataApp;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrantingDataApp.class)
@ContextConfiguration(classes = WebSecurityConfig.class)
public class UserRedirectionIntegrationTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

//	@Before
//	public void setup() {
//		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
//	}

}

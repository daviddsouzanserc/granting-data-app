package ca.gc.tri_agency.granting_data.app.config;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldif.LDIFReader;

@Configuration
public class EnvConfig {

	@Value("${ldap.testDataFile.nserc}")
	private String ldapTestDataFileNSERC;

	@Value("${ldap.testDataFile.sshrc}")
	private String ldapTestDataFileSSHRC;

	@Value("${ldap.url.sshrc}")
	private String ldapUrlSSHRC;

	@Value("${ldap.url.nserc}")
	private String ldapUrlNSERC;

	@Value("${ldap.base.dn.nserc}")
	private String ldapBaseDnNSERC;

	@Value("${ldap.base.dn.sshrc}")
	private String ldapBaseDnSSHRC;

	@Profile({ "local", "test"})
	@Bean
	public int setupLocalActiveDirectories() {
		// Create the configuration to use for the server.
		InMemoryDirectoryServerConfig config;
		InMemoryDirectoryServer sshrcDS;
		InMemoryDirectoryServer nsercDS;
		InMemoryListenerConfig nsercListenerConfig;
		InMemoryListenerConfig sshrcListenerConfig;
		// ClassLoader classLoader = getClass().getClassLoader();
		InputStream file;
		try {
			// config.addAdditionalBindCredentials("cn=Directory Manager", "password");
			config = new InMemoryDirectoryServerConfig(ldapBaseDnNSERC);
			nsercListenerConfig = new InMemoryListenerConfig("test", null, 8389, null, null, null);
			config.setListenerConfigs(nsercListenerConfig);
			nsercDS = new InMemoryDirectoryServer(config);
			// LDIFReader reader = new LDIFReader(reader)
			Resource resource = new ClassPathResource(ldapTestDataFileNSERC);
			file = resource.getInputStream();
			// file = new File(classLoader.getResource(ldapTestDataFileSSHRC).getFile());
			nsercDS.importFromLDIF(true, new LDIFReader(file));
			nsercDS.startListening();

		} catch (LDAPException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			config = new InMemoryDirectoryServerConfig(ldapBaseDnSSHRC);
			sshrcListenerConfig = new InMemoryListenerConfig("test", null, 9389, null, null, null);
			config.setListenerConfigs(sshrcListenerConfig);
			sshrcDS = new InMemoryDirectoryServer(config);
			Resource resource = new ClassPathResource(ldapTestDataFileSSHRC);
			file = resource.getInputStream();
			sshrcDS.importFromLDIF(true, new LDIFReader(file));
			sshrcDS.startListening();

		} catch (LDAPException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}

}

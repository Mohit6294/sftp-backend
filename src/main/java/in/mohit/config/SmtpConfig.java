package in.mohit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;

@Configuration
public class SmtpConfig {

	@Bean
	public DefaultSftpSessionFactory sftpSessionFactory() {
		DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
		factory.setHost("172.17.176.1");
		factory.setPort(22);
		factory.setUser("tester");
		factory.setPassword("password");
		factory.setTimeout(60000);
		factory.setAllowUnknownKeys(true);// Set to false if you have known_hosts setup
		return factory;
	}

	@Bean
	public SftpRemoteFileTemplate sftpRemoteFileTemplate(DefaultSftpSessionFactory sftpSessionFactory) {
		return new SftpRemoteFileTemplate(sftpSessionFactory);
	}


}

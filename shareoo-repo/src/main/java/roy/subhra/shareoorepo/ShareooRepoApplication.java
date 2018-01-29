package roy.subhra.shareoorepo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableMongoRepositories
@EnableWebMvc
public class ShareooRepoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShareooRepoApplication.class, args);
	}
}

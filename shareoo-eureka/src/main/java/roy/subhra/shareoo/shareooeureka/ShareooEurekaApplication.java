package roy.subhra.shareoo.shareooeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ShareooEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShareooEurekaApplication.class, args);
	}
}

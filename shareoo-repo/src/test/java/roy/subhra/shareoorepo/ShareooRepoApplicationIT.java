package roy.subhra.shareoorepo;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@RunWith(Cucumber.class)
@CucumberOptions(features ="src/test/resources", glue = "roy.subhra.shareoorepo.steps")
public class ShareooRepoApplicationIT {

}

package roy.subhra.shareoo.repo;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features ="src/test/resources", glue = "roy.subhra.repo.steps")
public class ShareooRepoApplicationIT {

}

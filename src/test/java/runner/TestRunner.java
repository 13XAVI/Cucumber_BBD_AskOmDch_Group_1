package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"steps", "hooks"},
//        tags = "@shipping",
        plugin = {
                "pretty",
                "json:target/cucumber.json",
                "html:target/cucumber-html-report"
        },
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
}

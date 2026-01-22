package hooks;

import factory.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import pages.*;

public class Hook {

    private WebDriver driver;

    @Before
    public void before() {
        driver = DriverFactory.initializeDriver("chrome");
    }


    @After
    public void after() {
        if (driver != null) {
            driver.quit();
        }
    }
}

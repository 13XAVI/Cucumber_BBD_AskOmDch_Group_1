package hooks;

import factory.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import pages.*;

public class Hook {

    private WebDriver driver;

    @Before
    public void before() {
        driver = DriverFactory.initializeDriver("chrome");
        PageFactory.initElements(driver, this);
    }


    @After
    public void after() {
        if (driver != null) {
            driver.quit();
        }
    }
}

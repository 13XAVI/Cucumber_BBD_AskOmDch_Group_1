package factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import utils.ConfigLoader;

public class DriverFactory {
    private static WebDriver driver;
    public static WebDriver initializeDriver(String browser){
        switch(browser){
            case "chrome":
                driver = new ChromeDriver();
                break;

            case "firefox":
                driver = new FirefoxDriver();
                break;
            default:
                throw new IllegalStateException("INVALID BROWSER" + browser);

        }
        driver.manage().window().maximize();
        driver.get(ConfigLoader.getInstance().getBaseUrl());
        return driver;
    }

    public static WebDriver getDriver(){
        return driver;
    }
}
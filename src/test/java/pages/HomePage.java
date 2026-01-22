package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "menu-item-1237")
    private WebElement accountMenu;

    @FindBy(id = "menu-item-1227")
    private WebElement storeMenu;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    private void clickMenu(WebElement menuItem) {
        wait.until(ExpectedConditions.elementToBeClickable(menuItem)).click();
    }

    public AccountPage clickAccount() {
        clickMenu(accountMenu);
        return new AccountPage(driver);
    }
}

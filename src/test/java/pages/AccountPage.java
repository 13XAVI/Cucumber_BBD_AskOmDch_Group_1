package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AccountPage {

    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(name = "login")
    private WebElement loginButton;

    @FindBy(id = "reg_username")
    private WebElement regUsernameField;

    @FindBy(id = "reg_email")
    private WebElement regEmailField;

    @FindBy(id = "reg_password")
    private WebElement regPasswordField;

    @FindBy(name = "register")
    private WebElement registerButton;

    @FindBy(css = ".wp-block-cover__inner-container h1")
    private WebElement accountHeader;

    @FindBy(xpath = "//p[contains(text(),'Hello')]")
    private WebElement welcomeText;

    @FindBy(css = ".woocommerce-error li")
    private WebElement errorMessage;


    public AccountPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        PageFactory.initElements(driver, this);
    }


    public AccountPage enterLoginUsername(String value) {
        usernameField.sendKeys(value);
        return this;
    }

    public AccountPage enterLoginPassword(String value) {
        passwordField.sendKeys(value);
        return this;
    }

    public AccountPage clickLogin() {
        loginButton.click();
        return this;
    }


    public AccountPage enterRegUsername(String value) {
        regUsernameField.sendKeys(value);
        return this;
    }

    public AccountPage enterRegEmail(String value) {
        regEmailField.sendKeys(value);
        return this;
    }

    public AccountPage enterRegPassword(String value) {
        regPasswordField.sendKeys(value);
        return this;
    }

    public AccountPage clickRegister() {
        registerButton.click();
        return this;
    }


    public String getWelcomeText() {
        return wait.until(ExpectedConditions.visibilityOf(welcomeText)).getText();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOf(errorMessage)).getText();
    }

    public String getAccountHeader() {
        return accountHeader.getText();
    }

}
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

    // Existing text elements
    @FindBy(css = ".wp-block-cover__inner-container h1")
    private WebElement accountHeader;

    @FindBy(xpath = "//p[contains(text(),'Hello')]")
    private WebElement welcomeText;

    @FindBy(css = ".woocommerce-error li")
    private WebElement errorMessage;


    @FindBy(linkText = "Lost your password?")
    private WebElement lostPasswordLink;

    @FindBy(id = "user_login")
    private WebElement resetUsernameOrEmailField;

    @FindBy(css = "button[value='Reset password']")
    private WebElement resetPasswordButton;

    @FindBy(css = ".woocommerce-message")
    private WebElement successMessage;

    @FindBy(css = ".woocommerce-error li")
    private WebElement resetErrorMessage;


    public AccountPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        PageFactory.initElements(driver, this);
    }

    // Existing login methods
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

    // Existing getter methods
    public String getWelcomeText() {
        return wait.until(ExpectedConditions.visibilityOf(welcomeText)).getText();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOf(errorMessage)).getText();
    }

    public String getAccountHeader() {
        return accountHeader.getText();
    }


    public AccountPage clickLostPassword() {
        wait.until(ExpectedConditions.elementToBeClickable(lostPasswordLink)).click();
        return this;
    }

    public AccountPage enterResetUsernameOrEmail(String value) {
        wait.until(ExpectedConditions.visibilityOf(resetUsernameOrEmailField)).clear();
        resetUsernameOrEmailField.sendKeys(value);
        return this;
    }

    public AccountPage clickResetPassword() {
        wait.until(ExpectedConditions.elementToBeClickable(resetPasswordButton)).click();
        return this;
    }

    public String getSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOf(successMessage)).getText();
    }

    public String getResetErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOf(resetErrorMessage)).getText();
    }

    public boolean isOnPasswordResetPage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(resetUsernameOrEmailField));
            return driver.getCurrentUrl().contains("lost-password") ||
                    resetUsernameOrEmailField.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProductDetailPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String expectedProductName;
    private String message;

    @FindBy(className = "product_title")
    private WebElement productTitle;

    @FindBy(name = "quantity")
    private WebElement quantityField;

    @FindBy(name = "add-to-cart")
    private WebElement addToCartButton;

    @FindBy(className = "woocommerce-message")
    private WebElement successMessage;

    @FindBy(className = "description_tab")
    private WebElement descriptionTab;

    @FindBy(className = "additional_information_tab")
    private WebElement additionalInformationTab;

    @FindBy(className = "reviews_tab")
    private WebElement reviewsTab;

    @FindBy(name = "comment")
    private WebElement commentField;

    @FindBy(name = "author")
    private WebElement authorField;

    @FindBy(name = "email")
    private WebElement emailField;

    @FindBy(id = "submit")
    private WebElement submitButton;

    @FindBy(css = ".description p")
    private WebElement commentDescription;

    @FindBy(css = ".wp-die-message p")
    private WebElement errorMessage;

    public ProductDetailPage(WebDriver driver, String productName) {
        this.driver = driver;
        this.expectedProductName = productName;
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToProduct(String productSlug) {
        driver.get("https://askomdch.com/product/" + productSlug + "/");
        wait.until(ExpectedConditions.visibilityOf(productTitle));
    }

    public boolean isOnProductPage() {
        return wait.until(ExpectedConditions.visibilityOf(productTitle)).getText().equals(expectedProductName);
    }

    public boolean addSelectedProductToCart(int quantity) {
        wait.until(ExpectedConditions.visibilityOf(quantityField));
        quantityField.clear();
        quantityField.sendKeys(String.valueOf(quantity));
        addToCartButton.click();

        message = wait.until(ExpectedConditions.visibilityOf(successMessage)).getText();
        message = message.substring(message.indexOf("\"") + 1);
        return !message.isEmpty();
    }

    public String getSuccessMessage() {
        return message;
    }

    public boolean clickTab(String tabName) {
        WebElement tab;
        switch (tabName.toLowerCase()) {
            case "description":
                tab = descriptionTab;
                break;
            case "additional information":
                tab = additionalInformationTab;
                break;
            case "reviews":
                tab = reviewsTab;
                break;
            default:
                throw new IllegalArgumentException("Unknown tab: " + tabName);
        }

        wait.until(ExpectedConditions.elementToBeClickable(tab)).click();
        wait.until(ExpectedConditions.attributeContains(tab, "class", "active"));
        return tab.getAttribute("class").contains("active");
    }

    public void fillReviewDetails(int rating, String comment, String name, String email) {
        if (rating >= 1 && rating <= 5) {
            driver.findElement(By.className("star-" + rating)).click();
            wait.until(ExpectedConditions.attributeContains(By.className("stars"), "class", "selected"));
        }

        if (comment != null && !comment.isEmpty()) {
            wait.until(ExpectedConditions.visibilityOf(commentField));
            commentField.clear();
            commentField.sendKeys(comment);
        }

        if (name != null && !name.isEmpty() && isElementPresent(authorField)) {
            authorField.clear();
            authorField.sendKeys(name);
        }

        if (email != null && !email.isEmpty() && isElementPresent(emailField)) {
            emailField.clear();
            emailField.sendKeys(email);
        }
    }

    public String submitReview() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();

        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.accept();
            return alertText;
        } catch (TimeoutException e0) {
            try {
                return wait.until(ExpectedConditions.visibilityOf(commentDescription)).getText();
            } catch (TimeoutException e1) {
                try {
                    return wait.until(ExpectedConditions.visibilityOf(errorMessage)).getText();
                } catch (TimeoutException e2) {
                    return "Timeout waiting for review submission response";
                }
            }
        }
    }

    private boolean isElementPresent(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
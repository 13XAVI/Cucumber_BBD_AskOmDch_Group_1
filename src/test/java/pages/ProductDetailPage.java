package pages;

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
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String productName;
    @FindBy(className = "product_title")
    private WebElement productTitle;

    @FindBy(name = "quantity")
    private WebElement quantityInput;

    @FindBy(name = "add-to-cart")
    private WebElement addToCartButton;

    @FindBy(className = "woocommerce-message")
    private WebElement successMessage;

    @FindBy(className = "additional_information_tab")
    private WebElement additionalInfoTab;

    @FindBy(className = "reviews_tab")
    private WebElement reviewsTab;

    @FindBy(name = "comment")
    private WebElement commentField;

    @FindBy(name = "author")
    private WebElement authorNameField;

    @FindBy(name = "email")
    private WebElement emailField;

    @FindBy(id = "wp-comment-cookies-consent")
    private WebElement saveDataCheckbox;

    @FindBy(id = "submit")
    private WebElement submitButton;

    @FindBy(className = "stars")
    private WebElement starsContainer;

    @FindBy(css = ".description p")
    private WebElement reviewDescription;

    @FindBy(css = ".wp-die-message p")
    private WebElement errorMessage;
    private final By productTitleLocator = By.className("product_title");
    private final By successMessageLocator = By.className("woocommerce-message");

    public ProductDetailPage(WebDriver driver, String productName) {
        this.driver = driver;
        this.productName = productName;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public boolean productNameCheck() {
        String actualProductName = wait.until(
                ExpectedConditions.visibilityOf(productTitle)
        ).getText();
        return productName.equals(actualProductName);
    }

    public String getProductName() {
        return wait.until(ExpectedConditions.visibilityOf(productTitle)).getText();
    }

    public boolean addSelectedProductToCart(int quantity) {
        wait.until(ExpectedConditions.visibilityOf(quantityInput));
        quantityInput.clear();
        quantityInput.sendKeys(String.valueOf(quantity));

        addToCartButton.click();

        WebElement messageElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(successMessageLocator)
        );
        String returnMessage = messageElement.getText();
        returnMessage = returnMessage.substring(returnMessage.indexOf("\"") + 1);

        if (quantity > 1) {
            return returnMessage.contains("\"" + productName + "\" have been added to your cart.");
        } else {
            return returnMessage.contains("\"" + productName + "\" has been added to your cart.");
        }
    }

    public String getAddToCartMessage() {
        return wait.until(ExpectedConditions.visibilityOf(successMessage)).getText();
    }

    public boolean additionalInformation() {
        wait.until(ExpectedConditions.elementToBeClickable(additionalInfoTab));
        additionalInfoTab.click();

        wait.until(ExpectedConditions.attributeContains(additionalInfoTab, "class", "active"));
        return additionalInfoTab.getAttribute("class").contains("active");
    }

    public boolean review() {
        wait.until(ExpectedConditions.elementToBeClickable(reviewsTab));
        reviewsTab.click();

        wait.until(ExpectedConditions.attributeContains(reviewsTab, "class", "active"));
        return reviewsTab.getAttribute("class").contains("active");
    }

    public ProductDetailPage reviewComment(String comment) {
        wait.until(ExpectedConditions.visibilityOf(commentField)).sendKeys(comment);
        return this;
    }

    /**
     * Enter reviewer name (if field is present)
     */
    public ProductDetailPage name(String authorName) {
        try {
            if (authorNameField.isDisplayed()) {
                authorNameField.sendKeys(authorName);
            }
        } catch (Exception e) {
            // Field not present (user might be logged in)
        }
        return this;
    }

    /**
     * Enter reviewer email (if field is present)
     */
    public ProductDetailPage email(String email) {
        try {
            if (emailField.isDisplayed()) {
                emailField.sendKeys(email);
            }
        } catch (Exception e) {
            // Field not present (user might be logged in)
        }
        return this;
    }

    public ProductDetailPage starRating(int rating) {
        By starLocator = By.className("star-" + rating);
        wait.until(ExpectedConditions.elementToBeClickable(starLocator)).click();
        wait.until(ExpectedConditions.attributeContains(starsContainer, "class", "selected"));
        return this;
    }

    public ProductDetailPage saveNameAndEmail() {
        if (!saveDataCheckbox.isSelected()) {
            saveDataCheckbox.click();
        }
        return this;
    }

    public String submitComment() {
        submitButton.click();
        if (isAlertPresent()) {
            return handleAlert();
        }
        if (isElementVisible(reviewDescription)) {
            return reviewDescription.getText();
        }

        if (isElementVisible(errorMessage)) {
            return errorMessage.getText();
        }

        return "No response message found";
    }

    private boolean isAlertPresent() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private String handleAlert() {
        var alert = driver.switchTo().alert();
        String alertText = alert.getText();
        alert.accept();
        return alertText;
    }

    private boolean isElementVisible(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
    /**
     * Navigate to product page by slug
     */
    public ProductDetailPage navigateToProduct(String productSlug) {
        driver.get("https://askomdch.com/product/" + productSlug + "/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(productTitleLocator));
        return this;
    }

    /**
     * Check if currently on the correct product page
     */
    public boolean isOnProductPage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(productTitle));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
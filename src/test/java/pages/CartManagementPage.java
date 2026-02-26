package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class CartManagementPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(css = ".ast-cart-menu-wrap .count")
    private WebElement cartCount;

    @FindBy(css = "tr.cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = "a.checkout-button.button.alt.wc-forward")
    private WebElement checkoutButton;

    @FindBy(css = ".woocommerce-message")
    private WebElement messageBox;

    @FindBy(css = ".cart-empty.woocommerce-info")
    private WebElement emptyCartMessage;

    public CartManagementPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public int getHeaderCartCount() {
        return Integer.parseInt(
                wait.until(ExpectedConditions.visibilityOf(cartCount))
                        .getText()
                        .trim()
        );
    }

    public void waitForCartCountToBe(int expectedCount) {
        wait.until(driver -> getHeaderCartCount() == expectedCount);
    }

    public String getEmptyCartMessage() {
        return wait.until(ExpectedConditions.visibilityOf(emptyCartMessage))
                .getText()
                .trim();
    }

    public String getRemovalConfirmationMessage() {
        return wait.until(ExpectedConditions.visibilityOf(messageBox))
                .getText()
                .split("Undo\\?")[0]
                .trim();
    }

    public void clickOnCheckoutButton() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
    }

    public void removeProductAndWaitForDecrement(String productName) {

        wait.until(ExpectedConditions.visibilityOfAllElements(cartItems));

        int previousCount = getHeaderCartCount();

        WebElement itemToRemove = cartItems.stream()
                .filter(item ->
                        item.findElement(org.openqa.selenium.By.cssSelector(".product-name a"))
                                .getText()
                                .trim()
                                .equalsIgnoreCase(productName)
                )
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Product not found in cart: " + productName)
                );

        WebElement removeButton =
                itemToRemove.findElement(org.openqa.selenium.By.cssSelector("a.remove"));

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", removeButton);

        wait.until(ExpectedConditions.elementToBeClickable(removeButton)).click();
        wait.until(ExpectedConditions.stalenessOf(itemToRemove));

        waitForCartCountToBe(previousCount - 1);
    }

    public void clearCartIfNotEmpty() {
        while (!cartItems.isEmpty()) {
            WebElement item = cartItems.get(0);
            WebElement removeButton =
                    item.findElement(org.openqa.selenium.By.cssSelector("a.remove"));

            removeButton.click();
            wait.until(ExpectedConditions.stalenessOf(item));
        }

        waitForCartCountToBe(0);
    }
}

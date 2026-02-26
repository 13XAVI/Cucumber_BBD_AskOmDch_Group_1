package steps;

import factory.DriverFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import hooks.Hook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.CartManagementPage;
import pages.HomePage;
import pages.StorePage;

import java.time.Duration;
import java.util.List;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CartManagementSteps {
    private int initialCartCount;
    private WebDriver driver = DriverFactory.getDriver();
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    private HomePage homePage = new HomePage(driver);
    private StorePage storePage = new StorePage(driver);
    private CartManagementPage cartPage = new CartManagementPage(driver);

    @Given("I am on the AskOmDch Store page")
    public void iAmOnTheStorePage() {
        homePage.clickToStorePage();
    }

    @When("I add {int} products to the cart")
    public void iAddProductsToTheCart(int items) {
        int initialCount = cartPage.getHeaderCartCount();

        for (int i = 1; i <= items; i++) {
            storePage.clickToAddToCart();
            cartPage.waitForCartCountToBe(initialCount + i);
        }
    }

    @When("I should be on the cart page")
    public void iOpenTheCartPage() {
        storePage.clickToViewCart();
    }

    @Then("I added a product to the cart")
    public void iAddedAProductToTheCart() {
        storePage.clickToAddToCart();
    }

    @Then("I should see item in the cart")
    public void iShouldSeeItemInTheCart() {
        storePage.clickToViewCart();
    }

    @Given("I have products in the cart")
    public void iHaveProductsInTheCart(List<String> productNames) {
        storePage.clickToViewCart();
        cartPage.clearCartIfNotEmpty();
        homePage.clickToStorePage();

        for (String productName : productNames) {
            storePage.addProductToCartByName(productName);
        }

        storePage.clickToViewCart();

        initialCartCount = cartPage.getHeaderCartCount();

        assertEquals(initialCartCount, productNames.size(),
                "Initial cart count mismatch. Expected: " + productNames.size() +
                        ", Found: " + initialCartCount);
    }

    @Then("the initial cart count should be {int}")
    public void theInitialCartCountShouldBe(int expectedCount) {
        assertEquals(initialCartCount, expectedCount, "Initial cart count mismatch");
    }

    @When("I remove the product {string}")
    public void iRemoveTheProduct(String productName) {
        cartPage.removeProductAndWaitForDecrement(productName);
    }

    @Then("the cart count should be {int}")
    public void cartCountShouldBe(int expectedCount) {
        int actualCount = cartPage.getHeaderCartCount();
        assertEquals(actualCount, expectedCount, "Cart count did not match");
    }

    @Then("I should see the product removed confirmation for {string}")
    public void iShouldSeeProductRemovedConfirmation(String productName) {
        String expectedMessage = "“" + productName + "” removed.";

        String actualMessage = cartPage.getRemovalConfirmationMessage()
                .replaceAll("\\s+", " ")
                .trim();

        assertEquals(actualMessage, expectedMessage,
                "Product removal confirmation mismatch");
    }

    @Then("I should see the empty cart message when cart is empty")
    public void iShouldSeeEmptyCartMessageWhenCartIsEmpty() {
        int count = cartPage.getHeaderCartCount();

        if (count == 0) {
            String expectedMessage = "Your cart is currently empty.";
            String actualMessage = cartPage.getEmptyCartMessage()
                    .replaceAll("\\s+", " ")
                    .trim();

            assertEquals(actualMessage, expectedMessage,
                    "Empty cart message mismatch");
        }
    }


    private double parseMoney(String text) {
        String cleaned = text.replaceAll("[^0-9.-]", "");
        return Double.parseDouble(cleaned);
    }

    private double getSubtotal() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("tr.cart-subtotal td .amount"))
        );
        return parseMoney(el.getText());
    }

    private double getDiscount() {
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("tr.cart-discount td .amount"))
            );
            return parseMoney(el.getText());
        } catch (Exception e) {
            return 0.0;
        }
    }

    private double getShipping() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("tr.shipping td .amount"))
        );
        return parseMoney(el.getText());
    }

    private double getOrderTotal() {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("tr.order-total td .amount"))
        );
        return parseMoney(el.getText());
    }

    @Given("I have {string} and {string} in cart with subtotal ${double}")
    public void iHaveProductsInCartWithSubtotal(String product1, String product2, double expectedSubtotal) {
        homePage.clickToStorePage();
        storePage.addProductToCartByName(product1);
        storePage.addProductToCartByName(product2);
        storePage.clickToViewCart();
        
        double actual = getSubtotal();
        assertEquals(actual, expectedSubtotal, 0.01, "Unexpected cart subtotal");
    }

    @When("I apply the coupon code {string}")
    public void iApplyTheCouponCode(String couponCode) {
        WebElement couponInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("coupon_code"))
        );
        couponInput.clear();
        couponInput.sendKeys(couponCode);

        WebElement applyButton = driver.findElement(By.name("apply_coupon"));
        applyButton.click();
    }

    @When("I remove any applied coupon")
    public void iRemoveAnyAppliedCoupon() {
        WebElement removeLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a.woocommerce-remove-coupon"))
        );
        removeLink.click();
    }

    @Then("the coupon {string} should be accepted")
    public void theCouponShouldBeAccepted(String couponCode) {
        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#post-1220 > div > div > div > div > div.woocommerce-notices-wrapper > div"))
        );
        String text = message.getText().toLowerCase();
        assertTrue(text.contains("coupon") || text.contains("applied") || text.contains("success"), 
                "Expected success message, got: " + text);
    }

    @Then("the coupon should be rejected")
    public void theCouponShouldBeRejected() {
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#post-1220 > div > div > div > div > div.woocommerce-notices-wrapper > ul > li"))
        );
        String text = error.getText().toLowerCase();
        assertTrue(text.contains("coupon"), "Expected error message to mention coupon");
    }

    @Then("the cart discount should be ${double}")
    public void theCartDiscountShouldBe(double expectedDiscount) {
        if (expectedDiscount == 0.0) {
            try {
                WebElement discountRow = driver.findElement(By.cssSelector("tr.cart-discount"));
                if (discountRow.isDisplayed()) {
                    double actual = getDiscount();
                    double absActual = Math.abs(actual);
                    assertEquals(absActual, expectedDiscount, 0.01, "Unexpected discount amount");
                }
            } catch (Exception e) {

            }
        } else {
            double actual = getDiscount();
            double absActual = Math.abs(actual);
            assertEquals(absActual, expectedDiscount, 0.01, "Unexpected discount amount");
        }
    }

    @Then("the shipping cost should be ${double}")
    public void theShippingCostShouldBe(double expectedShipping) {
        double actual = getShipping();
        assertEquals(actual, expectedShipping, 0.01, "Unexpected shipping cost");
    }

    @Then("the order total should be ${double}")
    public void theOrderTotalShouldBe(double expectedTotal) {
        double actual = getOrderTotal();
        assertEquals(actual, expectedTotal, 0.01, "Unexpected order total");
    }
}

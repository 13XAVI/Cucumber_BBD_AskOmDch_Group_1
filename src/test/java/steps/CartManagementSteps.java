package steps;

import factory.DriverFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import hooks.Hook;
import org.openqa.selenium.WebDriver;
import pages.CartManagementPage;
import pages.HomePage;
import pages.StorePage;

import java.util.List;
import static org.testng.Assert.assertEquals;

public class CartManagementSteps {
    private int initialCartCount;
    private WebDriver driver = DriverFactory.getDriver();
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
}

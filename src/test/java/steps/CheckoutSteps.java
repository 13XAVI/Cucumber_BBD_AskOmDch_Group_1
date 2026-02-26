package steps.checkout;

import domain.BillingDetails;
import factory.DriverFactory;
import hooks.Hook;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import pages.CartManagementPage;
import pages.CheckoutPage;
import pages.HomePage;
import pages.StorePage;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CheckoutSteps {

    private BillingDetails billing;
    private WebDriver driver = DriverFactory.getDriver();
    private StorePage storePage = new StorePage(driver);
    private HomePage homePage = new HomePage(driver);
    private CartManagementPage cartPage = new CartManagementPage(driver);
    private CheckoutPage checkoutPage = new pages.CheckoutPage(driver);


    @Given("I have a product in the cart")
    public void iHaveAProductInTheCart() {
        homePage.clickToStorePage();
        storePage.clickToAddToCart();
    }

    @And("I am on the checkout page")
    public void iAmOnTheCheckoutPage() {
        storePage.clickToViewCart();
        cartPage.clickOnCheckoutButton();
    }

    @When("I complete checkout with valid billing details:")
    public void iCompleteCheckoutWithValidBillingDetails(DataTable table) {

        Map<String, String> data = table.asMap(String.class, String.class);

        billing = new BillingDetails(
                data.get("First name"),
                data.get("Last name"),
                data.get("Company"),
                "Rwanda",
                data.get("Street"),
                data.get("City"),
                "Kigali Province",
                data.get("Postcode / ZIP"),
                data.get("Phone"),
                data.get("Email")
        );

        checkoutPage
                .fillBillingDetails(billing)
                .placeOrder();
    }

    @When("I attempt to place an order without {string}")
    public void iAttemptToPlaceAnOrderWithout(String field) {

        billing = new BillingDetails(
                "Tresor",
                "Xavier",
                "Gasabo",
                "Rwanda",
                "Gasabo",
                "Kigali",
                "Kigali Province",
                "000",
                "+250780000000",
                "test@gmail.com"
        );

        removeField(field);

        checkoutPage
                .fillBillingDetails(billing)
                .placeOrder();
    }

    private void removeField(String field) {
        switch (field.toLowerCase()) {
            case "first name":
                billing.setFirstName(null);
                break;
            case "last name":
                billing.setLastName(null);
                break;
            case "street address":
                billing.setAddress(null);
                break;
            case "town / city":
                billing.setCity(null);
                break;
            case "state":
                billing.setState(null);
                break;
            case "postcode / zip":
                billing.setPostcode(null);
                break;
            case "email":
                billing.setEmail(null);
                break;
            default:
                throw new IllegalArgumentException("Unknown field: " + field);
        }
    }

    @Then("I should see the validation message {string}")
    public void iShouldSeeTheValidationMessage(String expectedMessage) {
        List<String> errors = checkoutPage.getValidationMessages();
        assertTrue(
                errors.stream().anyMatch(e -> e.contains(expectedMessage)),
                "Expected validation message not found: " + expectedMessage
        );
    }

    @Then("I should see the order summary")
    public void iShouldSeeTheOrderSummary() {
        assertTrue(
                checkoutPage.getOrderSummary().size() > 0,
                "Order summary is empty"
        );
    }

    @Then("the order total should be correct")
    public void theOrderTotalShouldBeCorrect() {

        String totalLine = checkoutPage.getOrderSummary().stream()
                .filter(line -> line.toLowerCase().contains("total"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Total line not found"));

        assertTrue(
                totalLine.matches(".*\\d+.*"),
                "Order total seems incorrect: " + totalLine
        );
    }

    @Then("the order should be placed successfully")
    public void theOrderShouldBePlacedSuccessfully() {
        assertTrue(checkoutPage.isOrderPlacedSuccessfully());
        assertEquals(
                checkoutPage.getOrderSuccessMessage(),
                "Thank you. Your order has been received."
        );
    }
}

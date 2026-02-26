package steps;

import factory.DriverFactory;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.ProductDetailPage;
import pages.StorePage;

import java.time.Duration;

import static org.testng.Assert.*;

public class ProductDetailSteps {

    private WebDriver driver;
    private ProductDetailPage productDetailPage;
    private StorePage storePage;
    private String actualResponse;
    private WebDriverWait wait;

    public ProductDetailSteps() {
        this.driver = DriverFactory.getDriver();
        this.storePage = new StorePage(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Given("I am on the product detail page for {string}")
    public void iAmOnTheProductDetailPageFor(String productName) {
        productDetailPage = new ProductDetailPage(driver, productName);

        String productSlug = productName.toLowerCase().replace(" ", "-");

        productDetailPage.navigateToProduct(productSlug);

        assertTrue(productDetailPage.isOnProductPage(),
                "Not on the expected product page: " + productName);
    }

    @When("I add {int} item to cart")
    public void iAddItemToCart(int quantity) {
        boolean success = productDetailPage.addSelectedProductToCart(quantity);
        assertTrue(success, "Failed to add product to cart");
    }

    @When("I add {int} items to cart")
    public void iAddItemsToCart(int quantity) {
        boolean success = productDetailPage.addSelectedProductToCart(quantity);
        assertTrue(success, "Failed to add products to cart");
    }

    @Then("I should see the success message containing {string} has been added")
    public void iShouldSeeTheSuccessMessageContaining(String productName) {
        String successMessage = productDetailPage.getSuccessMessage();
        System.out.println(successMessage);
        assertTrue(
                successMessage.contains("“" + productName + "” has been added to your cart.") |
                successMessage.contains("“" + productName + "” have been added to your cart."),
                "Success message does not contain expected text"
        );
    }

    @Then("I should see the success message for {int} items")
    public void iShouldSeeTheSuccessMessageForItems(int quantity) {
        String successMessage = productDetailPage.getSuccessMessage();
        assertNotNull(successMessage, "Success message is null");
        assertFalse(successMessage.isEmpty(), "Success message is empty");

        if (quantity > 1) {
            assertTrue(successMessage.contains("have been added to your cart."),
                    "Success message does not contain 'have been added'");
        } else {
            assertTrue(successMessage.contains("has been added to your cart."),
                    "Success message does not contain 'has been added'");
        }
    }

    @When("I click on the Description tab")
    public void iClickOnTheDescriptionTab() {
        boolean isActive = productDetailPage.clickTab("description");
        assertTrue(isActive, "Description tab is not active after clicking");
    }

    @Then("the Description tab should be active")
    public void theDescriptionTabShouldBeActive() {
        // Verification already done in When step
    }

    @When("I click on the Additional Information tab")
    public void iClickOnTheAdditionalInformationTab() {
        boolean isActive = productDetailPage.clickTab("additional information");
        assertTrue(isActive, "Additional Information tab is not active after clicking");
    }

    @Then("the Additional Information tab should be active")
    public void theAdditionalInformationTabShouldBeActive() {
        // Verification already done in When step
    }

    @And("I click on the Reviews tab")
    public void iClickOnTheReviewsTab() {
        boolean isActive = productDetailPage.clickTab("reviews");
        assertTrue(isActive, "Reviews tab is not active after clicking");
    }

    @When("I leave my review with rating {int}, comment {string}, name {string}, and email {string}")
    public void iLeaveMyReviewWithRatingCommentNameAndEmail(int rating, String comment,
                                                            String name, String email) {
        productDetailPage.fillReviewDetails(rating, comment, name, email);
    }

    @And("I submit the review")
    public void iSubmitTheReview() {
        actualResponse = productDetailPage.submitReview();
    }

    @Then("I should see a review submission response")
    public void iShouldSeeAReviewSubmissionResponse() {
        assertNotNull(actualResponse, "Review submission response is null");
        assertFalse(actualResponse.isEmpty(), "Review submission response is empty");
        assertFalse(actualResponse.contains("Timeout waiting for review submission response"),
                "Review submission timed out");
    }

    @And("the product Search Header Should Be String  {string}")
    public void theProductSearchHeaderShouldBeStringSearchText() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("the product Search Header Should Be String {string}")
    public void theProductSearchHeaderShouldBeString(String arg0) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
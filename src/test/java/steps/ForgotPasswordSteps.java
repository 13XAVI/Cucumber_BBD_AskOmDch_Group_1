package steps;

import factory.DriverFactory;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import pages.AccountPage;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ForgotPasswordSteps {
    private WebDriver driver = DriverFactory.getDriver();
    private AccountPage accountPage = new AccountPage(driver);

    @When("I click on {string} link")
    public void iClickOnLink(String linkText) {
        if (linkText.equals("Lost your password?")) {
            accountPage.clickLostPassword();
        }
    }

    @Then("I should be on the password reset page")
    public void iShouldBeOnPasswordResetPage() {
        assertTrue(accountPage.isOnPasswordResetPage(),
                "User is not on the password reset page");
    }

    @When("I enter username or email {string}")
    public void iEnterUsernameOrEmail(String usernameOrEmail) {
        if (usernameOrEmail != null && !usernameOrEmail.isEmpty()) {
            accountPage.enterResetUsernameOrEmail(usernameOrEmail);
        }
    }

    @When("I click Reset password button")
    public void iClickResetPasswordButton() {
        accountPage.clickResetPassword();
    }

    @Then("I should see the success message {string}")
    public void iShouldSeeSuccessMessage(String expectedMessage) {
        String actualMessage = accountPage.getSuccessMessage().trim();
        assertTrue(actualMessage.contains(expectedMessage),
                "Expected success message to contain: " + expectedMessage +
                        ", but got: " + actualMessage);
    }

    @Then("I should see the reset error message {string}")
    public void iShouldSeeResetErrorMessage(String expectedMessage) {
        String actualMessage = accountPage.getResetErrorMessage().trim();
        assertEquals(actualMessage, expectedMessage,
                "Expected error message: " + expectedMessage +
                        ", but got: " + actualMessage);
    }
}
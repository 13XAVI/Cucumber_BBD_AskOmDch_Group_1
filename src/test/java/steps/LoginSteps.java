package steps;

import factory.DriverFactory;
import hooks.Hook;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import pages.AccountPage;
import pages.HomePage;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class LoginSteps {
    private WebDriver driver = DriverFactory.getDriver();
    private HomePage homePage = new HomePage(driver);
    private AccountPage accountPage = new AccountPage(driver);
    @Given("I am on the AskOmDch Account page on login")
    public void iAmOnTheAskOmDchAccountPage() {
        homePage.clickAccount();
    }


    @When("I login with username {string} and password {string}")
    public void loginWithUsernameAndPassword(String username, String password) {


        accountPage.enterLoginUsername(username)
                .enterLoginPassword(password)
                .clickLogin();
    }

    @Then("I should be logged in successfully")
    public void iShouldBeLoggedInSuccessfully() {

        String welcomeText = accountPage.getWelcomeText().trim();
        assertTrue(welcomeText.contains("Welcome") || welcomeText.contains("Hello"),
                "User was not logged in successfully. Actual message: " + welcomeText);


        String logoutText = accountPage.getWelcomeText().trim();
        assertTrue(logoutText.contains("Log out"),
                "Logout link not visible after login. Actual text: " + logoutText);

    }

    @Then("I should see {string} and {string}")
    public void iShouldSeeWelcomeAndLogout(String expectedWelcome, String expectedLogout) {
        String actualWelcome = accountPage.getWelcomeText().trim();
        String actualLogout = accountPage.getWelcomeText().trim();

        assertTrue(actualWelcome.contains(expectedWelcome),
                "Expected welcome text: " + expectedWelcome + ", but got: " + actualWelcome);
        assertTrue(actualLogout.contains(expectedLogout),
                "Expected logout text: " + expectedLogout + ", but got: " + actualLogout);
    }

    @When("I attempt to login with username {string} and password {string}")
    public void loginWithInvalidCredentials(String username, String password) {

        if (username != null && !username.isEmpty()) {
            accountPage.enterLoginUsername(username);
        }

        if (password != null && !password.isEmpty()) {
            accountPage.enterLoginPassword(password);
        }
        accountPage.clickLogin();
    }

    @Then("I should see the login error message {string}")
    public void iShouldSeeErrorMessage(String expectedMessage) {
        String actualMessage = accountPage.getErrorMessage().trim();
        assertEquals(actualMessage, expectedMessage,
                "Expected error message: " + expectedMessage + ", but got: " + actualMessage);
    }
}

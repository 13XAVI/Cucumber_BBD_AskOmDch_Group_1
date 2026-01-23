package steps;

import factory.DriverFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import hooks.Hook;
import org.openqa.selenium.WebDriver;
import pages.AccountPage;
import pages.HomePage;
import utils.UniqueFields;


import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class RegistrationSteps {
     private WebDriver driver = DriverFactory.getDriver();
    private HomePage homePage = new HomePage(driver);
    private AccountPage accountPage = new AccountPage(driver);

    @Given("I am on the AskOmDch Account page")
    public void iAmOnTheAskOmDchAccountPage() {
        homePage.clickAccount();
    }


    @When("I register with valid credentials")
    public void iRegisterWithValidCredentials(DataTable table) {

        for (Map<String, String> user : table.asMaps(String.class, String.class)) {
            String username = UniqueFields.generateUniqueUsername(user.get("username"));
            String email = UniqueFields.generateRandomEmail(user.get("email"));
            accountPage
                    .enterRegUsername(username)
                    .enterRegPassword(user.get("password"))
                    .enterRegEmail(email)
                    .clickRegister();
        }
    }


    @Then("my account should be created successfully")
    public void myAccountShouldBeCreatedSuccessfully() {
        assertTrue(
                accountPage.getWelcomeText().contains("Hello"),
                "Account was not created successfully"
        );
    }

    @Then("I should see a welcome message")
    public void iShouldSeeAWelcomeMessage() {
        assertTrue(
                accountPage.getWelcomeText().contains("Log out"),
                "Welcome message not displayed"
        );
    }


    @When("I register with existing email {string}")
    public void iRegisterWithExistingEmail(String email) {
        accountPage
                .enterRegUsername("TestUser")
                .enterRegPassword("Test@123")
                .enterRegEmail(email)
                .clickRegister();
    }


    @When("I submit the registration form without {string}")
    public void iSubmitTheRegistrationFormWithout(String field) {

        if (!field.equals("username")) {
            accountPage.enterRegUsername(
                    UniqueFields.generateUniqueUsername("TestUser")
            );
        }

        if (!field.equals("email")) {
            accountPage.enterRegEmail(
                    UniqueFields.generateRandomEmail("tester@gmail.com")
            );
        }

        if (!field.equals("password")) {
            accountPage.enterRegPassword("Test@123");
        }

        accountPage.clickRegister();
    }


    @When("I register with invalid email format {string}")
    public void iRegisterWithInvalidEmailFormat(String email) {
        accountPage
                .enterRegUsername("TestUser")
                .enterRegPassword("Test@123")
                .enterRegEmail(email)
                .clickRegister();
    }


    @Then("I should see the error message {string}")
    public void iShouldSeeTheErrorMessage(String expectedMessage) {
        String actualMessage = accountPage.getErrorMessage().trim();
        assertEquals(actualMessage, expectedMessage);
    }

    @Then("I should remain on the account page")
    public void iShouldRemainOnTheAccountPage() {
        assertTrue(
                accountPage.getAccountHeader().contains("Account"),
                "User navigated away from account page"
        );
    }
}

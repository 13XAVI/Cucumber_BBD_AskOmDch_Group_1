package pages;

import domain.BillingDetails;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class CheckoutPage {

    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "billing_first_name")
    private WebElement firstName;

    @FindBy(id = "billing_last_name")
    private WebElement lastName;

    @FindBy(id = "billing_company")
    private WebElement company;

    @FindBy(id = "billing_country")
    private WebElement country;

    @FindBy(id = "billing_address_1")
    private WebElement address;

    @FindBy(id = "billing_city")
    private WebElement city;

    @FindBy(id = "billing_state")
    private WebElement state;

    @FindBy(id = "billing_postcode")
    private WebElement postcode;

    @FindBy(id = "billing_phone")
    private WebElement phone;

    @FindBy(id = "billing_email")
    private WebElement email;

    @FindBy(id = "place_order")
    private WebElement placeOrderButton;

    @FindBy(css = ".woocommerce-error li")
    private List<WebElement> checkoutErrors;

    @FindBy(css = ".woocommerce-notice--success")
    private WebElement orderSuccessMessage;

    @FindBy(css = ".shop_table.order_details tbody tr")
    private List<WebElement> orderSummaryRows;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        PageFactory.initElements(driver, this);
    }

    public CheckoutPage fillBillingDetails(BillingDetails billing) {

        enterText(firstName, billing.getFirstName());
        enterText(lastName, billing.getLastName());
        enterText(company, billing.getCompany());

        selectFromDropdown(country, billing.getCountry());

        enterText(address, billing.getAddress());
        enterText(city, billing.getCity());

        selectFromDropdown(state, billing.getState());

        enterText(postcode, billing.getPostcode());
        enterText(phone, billing.getPhone());
        enterText(email, billing.getEmail());

        return this;
    }

    public CheckoutPage placeOrder() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", placeOrderButton);
        wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton)).click();
        return this;
    }


    public boolean isOrderPlacedSuccessfully() {
        return wait.until(ExpectedConditions.visibilityOf(orderSuccessMessage)).isDisplayed();
    }

    public String getOrderSuccessMessage() {
        return orderSuccessMessage.getText();
    }

    public List<String> getValidationMessages() {
        return checkoutErrors.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public List<String> getOrderSummary() {
        return orderSummaryRows.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    private void enterText(WebElement element, String value) {
        if (value != null) {
            element.clear();
            element.sendKeys(value);
        }
    }

    private void selectFromDropdown(WebElement element, String value) {
        if (value == null) return;

        if (element.getTagName().equalsIgnoreCase("select")) {
            new Select(element).selectByVisibleText(value);
        } else {
            element.click();
            element.sendKeys(value);
            element.sendKeys(Keys.ENTER);
        }
    }

    private void scrollTo(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", element);
    }
}

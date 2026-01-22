package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class StorePage {

    private WebDriver driver;
    private WebDriverWait wait;
    private By search_field = By.id("woocommerce-product-search-field-0");
    private By searchButton = By.cssSelector("button[value='Search']");

    private By addToCartButton = By.cssSelector("a.add_to_cart_button");
    private By cartContainerLink = By.cssSelector("a.cart-container");

    public StorePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }


    public StorePage clickToAddToCart() {
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        addButton.click();
        wait.until(driver -> addButton.getAttribute("class").contains("added"));
        return this;
    }

    public void clickToViewCart() {
        WebElement cartLink = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(cartContainerLink));
        cartLink.click();
    }


    public StorePage clickSearch() {
        driver.findElement(searchButton).click();
        return this;
    }

    public void addProductToCartByName(String productName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        List<WebElement> products = driver.findElements(
                By.cssSelector(".product")
        );

        for (WebElement product : products) {

            String name = product.findElement(By.cssSelector(".woocommerce-loop-product__title"))
                    .getText()
                    .trim();

            if (name.equalsIgnoreCase(productName)) {
                WebElement addButton = product.findElement(
                        By.cssSelector("a.add_to_cart_button, button.add_to_cart_button")
                );

                wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();

                wait.until(ExpectedConditions.attributeContains(
                        addButton, "class", "added"
                ));

                return;
            }

        }

        throw new RuntimeException("Product not found: " + productName);
    }


    public StorePage enterSearch(String search) {
        driver.findElement(search_field).clear();
        driver.findElement(search_field).sendKeys(search);
        return this;
    }

    public String getSearchResultsMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".woocommerce-info")
        )).getText().trim();

    }


    public boolean doesProductsContainKeyword(String keyword) {
        List<WebElement> products = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".products .product"))
        );

        for (int i = 0; i < products.size(); i++) {
            WebElement product = wait.until(ExpectedConditions.visibilityOf(products.get(i)));

            String productName = product.findElement(By.cssSelector(".woocommerce-loop-product__title"))
                    .getText().trim();
            String productCategory = product.findElement(By.cssSelector(".ast-woo-product-category"))
                    .getText().trim();

            if (!productName.toLowerCase().contains(keyword.toLowerCase()) &&
                    !productCategory.toLowerCase().contains(keyword.toLowerCase())) {
                return false;
            }
        }
        return true;
    }



}

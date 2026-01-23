package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class StorePage {

    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "woocommerce-product-search-field-0")
    private WebElement searchField;

    @FindBy(css = "button[value='Search']")
    private WebElement searchButton;

    @FindBy(tagName = "bdi")
    private List<WebElement> storeListPrices;

    @FindBy(css = "a.add_to_cart_button")
    private WebElement addToCartButton;

    @FindBy(css = "a.cart-container")
    private WebElement cartContainerLink;

    @FindBy(className = "orderby")
    private WebElement sortingField;

    @FindBy(id = "product_cat")
    private WebElement productCategoryDropdown;

    @FindBy(css = "ul.products li.product")
    private List<WebElement> productItems;

    @FindBy(css = "a.woocommerce-LoopProduct-link.woocommerce-loop-product__link")
    private List<WebElement> productLinks;

    @FindBy(css = ".woocommerce-info")
    private WebElement searchResultsMessage;

    @FindBy(css = "ul.products.columns-4")
    private WebElement productsContainer;

    @FindBy(css = ".products .product")
    private List<WebElement> allProducts;

    @FindBy(css = ".woocommerce-loop-product__title")
    private List<WebElement> productTitles;

    @FindBy(css = ".ast-woo-product-category")
    private List<WebElement> productCategories;

    @FindBy(css = "del bdi")
    private List<WebElement> deletedPrices;

    @FindBy(className = "woocommerce-no-products-found")
    private WebElement invalidSearchResponse;

    @FindBy(className = "product_title")
    private List<WebElement> productTitleElements;

    public StorePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public StorePage enterSearch(String search) {
        searchField.clear();
        searchField.sendKeys(search);
        return this;
    }

    public StorePage clickSearch() {
        searchButton.click();
        return this;
    }

    public String getSearchResultsMessage() {
        return wait.until(ExpectedConditions.visibilityOf(searchResultsMessage))
                .getText()
                .trim();
    }

    public void sorting(String selectorName) {
        Select selectOption = new Select(sortingField);
        sortingField.sendKeys(Keys.ENTER);
        selectOption.selectByVisibleText(selectorName);
    }

    public boolean doesProductsContainKeyword(String keyword) {
        try {
            List<WebElement> products = wait.until(
                    ExpectedConditions.visibilityOfAllElements(allProducts)
            );

            if (products.isEmpty()) {
                wait.until(ExpectedConditions.visibilityOf(invalidSearchResponse));
                return false;
            }

            for (WebElement product : products) {
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
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSortedByPrice(boolean sortedAsc) {
        List<WebElement> currentPrices = storeListPrices.stream()
                .filter(val -> !deletedPrices.contains(val))
                .toList();

        List<Double> prices = currentPrices.stream()
                .map(val -> Double.parseDouble(val.getText().replace("$", "")))
                .toList();

        for (int i = 0; i < prices.size() - 1; i++) {
            if (sortedAsc && prices.get(i) > prices.get(i + 1)) return false;
            if (!sortedAsc && prices.get(i) < prices.get(i + 1)) return false;
        }
        return true;
    }

    public void selectCategoryByValue(String value) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(productCategoryDropdown));
        Select select = new Select(dropdown);
        select.selectByValue(value);
        wait.until(ExpectedConditions.visibilityOfAllElements(productItems));
    }

    public List<WebElement> getVisibleProductsByCategory(String category) {
        String categorySlug = category.toLowerCase()
                .replace("'", "")
                .replace("'", "")
                .replace(" ", "-");

        wait.until(ExpectedConditions.visibilityOf(productsContainer));
        wait.until(ExpectedConditions.visibilityOfAllElements(productItems));

        return productItems.stream()
                .filter(WebElement::isDisplayed)
                .filter(product -> {
                    String productClass = product.getAttribute("class").toLowerCase();
                    return productClass.contains("product_cat-" + categorySlug);
                })
                .collect(Collectors.toList());
    }

    public int getDisplayedProductCountByCategory(String category) {
        return getVisibleProductsByCategory(category).size();
    }
}
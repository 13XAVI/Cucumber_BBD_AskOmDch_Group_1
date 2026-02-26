package steps;

import factory.DriverFactory;
import hooks.Hook;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.HomePage;
import pages.StorePage;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ProductSteps {
    private WebDriver driver = DriverFactory.getDriver();
    private  StorePage storePage = new StorePage(driver);
    private HomePage homePage = new HomePage(driver);
    private boolean sortProductResult;
    private String filterByPriceResponce;
    private boolean priceRangeResult;
    private String priceRangeResultMessage;


    @Given("I am on the AskOmDch Store page To Browse Product")
    public void iAmOnTheStorePage() {
        homePage.clickToStorePage();
    }

    @When("I enter a search keyword {string} in the search box")
    public void iEnterASearchKeywordInTheSearchBox(String keyword) {
        storePage.enterSearch(keyword);
    }

    @And("I click the search button")
    public void iClickTheSearchButton() {
        storePage.clickSearch();
    }

    @Then("I should see only products that contain {string} in their name or category")
    public void onlyProductsThatContainNameOrDescription(String productName) {
        boolean allProduct = storePage.doesProductsContainKeyword(productName);
        assertTrue(allProduct, "Some products do not contain keyword: " + productName);
    }


    @Then("I should see a message {string}")
    public void searchForInvalidProduct(String expectedMessage) {
        String actualMessage = storePage.getSearchResultsMessage();
        assertEquals(actualMessage, expectedMessage, "The displayed message is not as expected.");
    }



    @When("I sort products By price")
    public void i_sort_products_using_these_options(DataTable dataTable) {
        List<Map<String, String>> sortOptions = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : sortOptions) {
            String sortOption = row.get("sort_option");
            storePage.sorting(sortOption);

            if (sortOption.equals("Price: low to high")) {
                sortProductResult = storePage.isSortedByPrice(true);
                filterByPriceResponce = "Products are not sorted ascendingly by their prices.";
            } else if (sortOption.equals("Price: high to low")) {
                sortProductResult = storePage.isSortedByPrice(false);
                filterByPriceResponce = "Products are not sorted descendingly by their prices.";
            }
        }
    }

    @Then("The products are sorted in the desired order")
    public void areProductSorted(){
        if(filterByPriceResponce != null) assertTrue(sortProductResult, filterByPriceResponce);
    }

    @When("I select and click category {string} from the category filter")
    public void selectCategoryFilter(String category) {
        String categoryValue = category.toLowerCase()
                .replace("'", "")
                .replace("'", "")
                .replace(" ", "-");
        storePage.selectCategoryByValue(categoryValue);
    }



    @Then("I should see {int} products in the {string} category")
    public void iShouldSeeProductsInCategory(int expectedCount, String category) {
        int actualCount = storePage.getDisplayedProductCountByCategory(category);

        assertEquals(
                actualCount,
                expectedCount,
                "Expected product count: " + expectedCount +
                        ", but found: " + actualCount +
                        " products for category: " + category
        );
    }

    @When("I filter products within the price range")
    public void i_filter_products_by_the_following_price_ranges(DataTable dataTable) {
        List<Map<String, String>> priceRanges = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : priceRanges) {
            int minPrice = Integer.parseInt(row.get("min_price"));
            int maxPrice = Integer.parseInt(row.get("max_price"));
            priceRangeResult = storePage.filterByPrice(minPrice, maxPrice);
            priceRangeResultMessage = "Should find products in price range " + minPrice + " to " + maxPrice;
            Assert.assertTrue(priceRangeResult, priceRangeResultMessage);
        }
    }

    @Then("I should see only products within the specified price range")
    public void iShouldSeeOnlyProductsWithinTheSpecifiedPriceRange() {
        Assert.assertTrue(priceRangeResult, priceRangeResultMessage);
    }

}
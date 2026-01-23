@productBrowsing @store
Feature: Product Browsing
  As a user
  I want to search and filter products
  So that I can quickly find products I am interested to buy

  Background:
    Given I am on the AskOmDch Store page To Browse Product

  @smoke @positive
  Scenario Outline: Search for products by keyword
    When I enter a search keyword "<keyword>" in the search box
    And I click the search button
    Then I should see only products that contain "<keyword>" in their name or category

    Examples:
      | keyword    |
      | Tshirt     |
      | Dark Brown |
      | Shoes      |

  @regression @negative
  Scenario: Search for an invalid product
    When I enter a search keyword "InvalidProduct" in the search box
    And I click the search button
    Then I should see a message "No products were found matching your selection."

  @sort
  Scenario: Sort products using different options
    When I sort products By price
      | sort_option                |
      | Sort by price: low to high |
      | Sort by price: high to low |
    Then The products are sorted in the desired order
  @filter
  Scenario: Filter products by different price ranges
    When I filter products by the following price ranges:
      | min_price | max_price |
      | 10        | 50        |
      | 50        | 100       |
      | 100       | 150       |
    Then I should see only products within the specified price range
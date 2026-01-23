Feature: Product Detail Page Functionality
  As a customer
  I want to view product details and add products to cart
  So that I can make purchases from the store

  Background:
    Given I am on the product detail page for "Blue Shoes"

  @add-to-cart @smoke
  Scenario: Add single product to cart
    When I add 1 item to cart
    Then I should see the success message containing "Blue Shoes" has been added

  @add-to-cart
  Scenario Outline: Add different quantities to cart
    When I add <quantity> items to cart
    Then I should see the success message for <quantity> items

    Examples:
      | quantity |
      | 1        |
      | 2        |
      | 5        |
      | 10       |

  @description-tab @smoke
  Scenario: View product description tab
    When I click on the Description tab
    Then the Description tab should be active

  @additional-info-tab @smoke
  Scenario: View additional information tab
    When I click on the Additional Information tab
    Then the Additional Information tab should be active

  @review
  Scenario Outline: Submit reviews with different ratings
    And I click on the Reviews tab
    When I leave my review with rating <rating>, comment "<comment>", name "<name>", and email "<email>"
    And I submit the review
    Then I should see a review submission response

    Examples:
      | rating | comment                              | name          | email                  |
      | 5      | Excellent product!                   | Alice Brown   | alice@example.com      |
      | 4      | Very good, would recommend           | Bob Wilson    | bob@example.com        |
      | 3      | Average quality, okay for the price  | Carol Davis   | carol@example.com      |
      | 2      | Below expectations                   | David Miller  | david@example.com      |
      | 1      | Not satisfied with this purchase     | Eve Anderson  | eve@example.com        |